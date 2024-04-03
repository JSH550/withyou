package com.js.withyou.service.impl;

import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostListDto;
import com.js.withyou.data.dto.PostViewDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Post;
import com.js.withyou.repository.PostRepository;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@Slf4j

public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;

    public PostServiceImpl(PostRepository postRepository, MemberService memberService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
    }


    @Transactional
    @Override
    public boolean createPost(PostCreateDto postCreateDto, String memberEmail) {
        try {
            //멤버 entity 조회
            Member member = memberService.getMemberEntityByEmail(memberEmail);
            Post post = Post.builder()
                    .postTitle(postCreateDto.getPostTitle())
                    .postContent(postCreateDto.getPostContent())
                    .member(member)
                    .build();
            postRepository.save(post);
            return true;
        } catch (Exception e){
            log.info("post 저장 실패");
            return false;
        }
    }

    /**
     * 페이지네이션으로 postList를 반환하는 메서드 입니다.
     *
     * @param pageNumber
     * @param pageSize
     * @return PostListDto
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PostListDto> getPostList(int pageNumber, int pageSize) {
        // pageNumber는 0부터 시작하므로 1을 빼줍니다.
        pageNumber -= 1;

        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize);
        /**
         * 전달받은 page 정보로 Post를 조회해서 반환합니다.
         * 반환된 객체는 Page 타입임으로 안에 데이터를 꺼내야 합니다.
         */
        Page<Post> allPosts = postRepository.findAll(pageRequest);
        //PostListDto 저장용 객체입니다.
        List<PostListDto> postListDtos = new ArrayList<>();
        //repository에서 반환된 값을 PlstListDTo로 변경하여 반환합니다.
       postListDtos = allPosts.getContent()
                .stream()
                .map(Post -> {
                    return PostListDto.builder()
                            .postTitle(Post.getPostTitle())
                            .postId(Post.getPostId())
                            .memberName(Post.getMember().getMemberName())
                            .build();
                }).collect(Collectors.toList());
        // Page 객체로 결과를 래핑하여 반환합니다.
        // 파라미터는, 전달할객체,pageRequest 객체, total elements 순입니다.
        PageImpl<PostListDto> postListDtos1 = new PageImpl<>(postListDtos, pageRequest, allPosts.getTotalElements());
        return postListDtos1;
    }

    @Override
    public PostViewDto getPostById(Long postId) {
        Optional<Post> result = postRepository.findById(postId);
        if (result.isEmpty()){
            throw new NoSuchElementException("post 검색 결과가 없습니다.");
        }
        //작성 시간 포맷을 설정합니다. 연 월 일 시간 분 으로 나타냅니다.
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        Post post = result.get();
//        String format = post.getPostCreateDate().format(dateTimeFormatter);
        return PostViewDto.builder()
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postId(post.getPostId())
                .postCreateDate(post.getPostCreateDate().format(dateTimeFormatter))
                .memberName(post.getMember().getMemberName())
                .build();
    }
}

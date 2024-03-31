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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<PostListDto> getPostList(Integer pageNumber) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostListDto> getPostList(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber,pageSize);
        /**
         * 전달받은 page 정보로 Post를 조회해서 반환합니다.
         * 반환된 객체는 Page 타입임으로 안에 데이터를 꺼내야 합니다.
         */
        Page<Post> allPosts = postRepository.findAll(pageRequest);
        //return용 객체입니다.
        List<PostListDto> postListDtos = new ArrayList<>();
        //repository에서 반환된 값을 PlstListDTo로 변경하여 반환합니다.
       return postListDtos = allPosts.getContent()
                .stream()
                .map(Post -> {
                    return PostListDto.builder()
                            .postTitle(Post.getPostTitle())
                            .postId(Post.getPostId())
                            .memberName(Post.getMember().getMemberName())
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    public PostViewDto getPostById(Long postId) {
        return null;
    }
}

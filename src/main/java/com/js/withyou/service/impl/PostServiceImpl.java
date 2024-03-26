package com.js.withyou.service.impl;

import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostViewDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Post;
import com.js.withyou.repository.PostRepository;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PostService;
import org.springframework.stereotype.Service;

@Service

public class PostServiceImpl implements PostService {

  private final   PostRepository postRepository;
    private final MemberService memberService;
    public PostServiceImpl(PostRepository postRepository, MemberService memberService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    @Override
    public void createPost(PostCreateDto postCreateDto,String memberEmail) {
        Member member = memberService.getMemberEntityByEmail(memberEmail);

        Post post = Post.builder()
                .postTitle(postCreateDto.getPostTitle())
                .postContent(postCreateDto.getPostContent())
                .member(member)
                .build();

        postRepository.save(post);


    }

    @Override
    public PostViewDto getPostById(Long postId) {
        return null;
    }
}

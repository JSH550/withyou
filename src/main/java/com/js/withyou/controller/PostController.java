package com.js.withyou.controller;


import com.js.withyou.data.dto.PostViewDto;
import com.js.withyou.service.PostService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@Slf4j
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{postId}")
    public void showPostDetail(@PathVariable String postId){
        PostViewDto postById = postService.getPostById(Long.valueOf(postId));


    }

    @PostMapping("/posts")
    public void savePost(@SessionAttribute(name = "MEMBER_LOGIN_EMAIL",required = false) String memberEmail){
        postService.createPost();


    }



}

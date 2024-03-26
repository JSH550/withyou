package com.js.withyou.service;

import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostViewDto;

public interface PostService {
    void createPost(PostCreateDto postCreateDto,String memberEmail);

    PostViewDto getPostById(Long postId);
}

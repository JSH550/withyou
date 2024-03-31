package com.js.withyou.service;

import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostListDto;
import com.js.withyou.data.dto.PostViewDto;

import java.util.List;

public interface PostService {

    /**
     * post를 저장하는 메서드 입니다.
     *
     * @param postCreateDto
     * @param memberEmail
     * @return
     */
    boolean createPost(PostCreateDto postCreateDto, String memberEmail);

    List<PostListDto> getPostList(int pageNumber, int pageSize);

    PostViewDto getPostById(Long postId);
}

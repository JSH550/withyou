package com.js.withyou.service;

import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostListDto;
import com.js.withyou.data.dto.PostViewDto;
import org.springframework.data.domain.Page;

public interface PostService {

    /**
     * post를 저장하는 메서드 입니다.
     *
     * @param postCreateDto
     * @param memberEmail
     * @return boolean
     */
    boolean createPost(PostCreateDto postCreateDto, String memberEmail);

    /**
     * 페이지네이션으로 postList를 반환하는 메서드 입니다.
     *
     * @param pageNumber
     * @param pageSize
     * @return PostListDto
     */
    Page<PostListDto> getPostList(int pageNumber, int pageSize);

    PostViewDto getPostById(Long postId);

}

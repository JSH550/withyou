package com.js.withyou.controller;


import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.PostDto;
import com.js.withyou.data.dto.PostCreateDto;
import com.js.withyou.data.dto.PostListDto;
import com.js.withyou.data.dto.PostViewDto;
import com.js.withyou.service.PostService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }




    @GetMapping("/posts")
    public String showMedicalInfoMainPage(){


        return "/post/post-home";




    }

    @GetMapping("/posts/get-post-data")
    public ResponseEntity<List<PostListDto>> returnPostData (){


        Page<PostListDto> postList = postService.getPostList(0,10);
      return ResponseEntity.ok(postList.getContent());


    }
    @GetMapping("/posts/{postId}")
    public String showPostDetail(@PathVariable(required = false) String postId,
                                 Model model) {
        PostViewDto postById = postService.getPostById(Long.valueOf(postId));
        log.info("post 조회결과={}", postById.toString());

        if (postById == null) {
            return "NG";
        }
        model.addAttribute("post", postById);

        return "/post/post-detail";


    }

    @GetMapping("/posts/new")
    public String showPostWritePage() {

        return "/post/post-write";

    }

    //@ToDo 게시물 검색 기능, 내가쓴글 보기, 댓글기능

    /**
     * post list를 보여주는 API 입니다.
//     *
//     * @param page  유저가 요청한 페이지 입니다. null이거나 0미만이면 1페이지를 보여줍니다.
//     * @param size
//     * @param model
     * @return
     */
//    @GetMapping("/posts")
//    public String showPostList(@RequestParam(value = "page", required = false) Integer page,
//                               @RequestParam(value = "size", required = false) Integer size,
//                               Model model) {
//        if (page == null || page <= 0) {//유저가 요청한 page가 null 이거나 0미만이면 1페이지 보여줌
//            page = 1;
//        }
//        if (size == null || size < 10) {//유저가 요청한 pageSize가 null 이거나 10 미만이면 10개 보여줌
//            size = 10;
//        } else if (size > 30) {//유저가 요청한 pageSize가 30 초과면 30으로 변경
//            size = 30;
//        }
//
//        log.info("요청 페이지 ={}, 요청 size={}", page, size);
////        List<PostListDto> postList = postService.getPostList(page, size);
//        Page<PostListDto> postListDtoPage = postService.getPostList(page, size);
//        for (PostListDto postListDto : postListDtoPage) {
//            log.info("찾은값 ={}", postListDto.toString());
//        }
//
//
//        log.info("totalPageNumber={}", postListDtoPage.getTotalPages());
//        model.addAttribute("postList", postListDtoPage);
//        return "/post/post-list";
//    }


    @ResponseBody
    @PostMapping("/posts")
    public ResponseEntity<String> savePost(@Validated @RequestBody PostCreateDto postCreateDto
            , BindingResult bindingResult
            , Authentication authentication) {

        //웹브라우저에서 제목과 내용에 대해 유효성 검사를 진행합니다.
        //서버단에서 유효성 에러 발생시 잘못된 요청입니다.
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");

        }
        if (authentication == null) {
            return ResponseEntity.badRequest().body("로그인정보가 없습니다.");
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        //로그인 유무를 체크합니다.

        //유저정보랑 글정보 넘겨서 저장해주세요~
        postService.createPost(postCreateDto, customUser.getUsername());

//        postService.createPost();
        return ResponseEntity.ok().body("ok");
    }


}

package com.js.withyou.controller;

import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.dto.review.ReviewCreateDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.review.ReviewDeleteDto;
import com.js.withyou.data.dto.review.ReviewDto;
import com.js.withyou.data.dto.review.ReviewUpdateDto;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final PlaceService placeService;

    private final MemberService memberService;

    public ReviewController(ReviewService reviewService, PlaceService placeService, MemberService memberService) {

        this.reviewService = reviewService;
        this.placeService = placeService;
        this.memberService = memberService;
    }

    /**
     * 시설 review 작성을 위한 페이지를 보여주는 메서드입니다,
     * @param placeId 시설의 ID 값입니다. 쿼리파라미터로 받습니다.
     * @param model   view로 데이터를 보내기 위한 model입니다.
     * @return
     */
    @GetMapping("/review")
    public String showReviewForm(@RequestParam(name = "placeId") Long placeId,
                                 Authentication authentication,
                                 Model model) {
        if (authentication.isAuthenticated()) {

            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            PlaceDto placeByPlaceId = placeService.getPlaceByPlaceId(placeId);//ID로 조회된 시설이 없으면 에러 페이지로 던집니다.

            model.addAttribute("placeDto", placeByPlaceId);
            model.addAttribute("member", customUser.memberName);//요청 유저의 이름을 저장합니다.
            log.info("리뷰페이지 조회 유저={}", authentication.getName());
            return "/place/place-review-form";

        } else {
            log.info("로그인 하지 않은 사용자 login page로 이동합니다.");
            return "redirect:/login";
        }

        //유저 정보를 가져옵니다.
    }

    /**
     * 리뷰 수정 폼으로 이동하는 API 입니다.
     *
     * @param id             리뷰의 PK입니다.
     * @param authentication 유저 인증 정보를 확인합니다.
     * @return
     */
    @GetMapping("/reviews/{id}")
    public String showReviewEditForm(@PathVariable Long id
            , Authentication authentication
            , Model model) {
        //인증 정보가 없을시 로그인 페이지로 리다이렉트
        if (!authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        log.info("리뷰 작성한 유저 이메일={}", customUser.getUsername());

        //리뷰 작성자와, 작업 요청자가 동일하면 review의 내용 반환, 다를경우 예외로 던집니다.
        ReviewDto reviewDto = reviewService.validateReviewOwnership(id, customUser.getUsername());
        model.addAttribute("review", reviewDto);
        return "/review/review-edit-form";

    }

    /**
     * 리뷰를 수정하는 API입니다.
     * @param id review의 PK입니다.
     * @param reviewUpdateDto
     * @param authentication
     * @return
     */
    @PatchMapping("/reviews/{id}")
    public ResponseEntity<String> editReview(@PathVariable Long id,
                                             @RequestBody ReviewUpdateDto reviewUpdateDto,
                                             Authentication authentication) {

        //PathVariable id값과 DTO 의 id 값이 일치한지 확인합니다.
        if (!id.equals(reviewUpdateDto.getReviewId())) {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
        //인증 정보가 없을시 로그인 페이지로 리다이렉트
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인 정보가 없습니다.");
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        log.info("리뷰 작성한 유저 이메일={}", customUser.getUsername());
        reviewUpdateDto.setMemberEmail(customUser.getUsername());

        Boolean result = reviewService.updateReview(reviewUpdateDto);

        //업데이트 결과가 true 일때만 HTTP 200 아닐경우 서버에러
        if (result){
            return ResponseEntity.ok().body("ok");
        }
        return ResponseEntity.internalServerError().body("알수없는 서버에러 관리자에게 문의해주세요");

    }


    @ResponseBody
    @PostMapping("/review")
    public ResponseEntity<String> saveReview(@RequestBody ReviewCreateDto reviewCreateDto,
                                             Authentication authentication) {
        if (authentication.isAuthenticated()) {
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            log.info("리뷰 작성한 유저 이메일={}", customUser.getUsername());
            reviewCreateDto.setUserEmail(customUser.getUsername());
        } else {
            return ResponseEntity.badRequest().body("로그인정보가 없습니다. 로그인해주세요!");
        }
//
        log.info(reviewCreateDto.toString());
        reviewService.createReview(reviewCreateDto);

        return ResponseEntity.ok("ok");
    }

    /**
     * 리뷰(review)를 삭제하는 REST API 입니다.
     *
     * @param reviewDeleteDto
     * @param authentication
     * @return
     */
    @ResponseBody
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@RequestBody ReviewDeleteDto reviewDeleteDto
            , Authentication authentication) {

        log.info("리뷰 삭제 요청 삭제 요청자 ={}, 삭제 요청된 리뷰 ={}", reviewDeleteDto.getMemberEmail(), reviewDeleteDto.getReviewId());

        boolean validResult = memberService.validateUserAuthentication(authentication, reviewDeleteDto.getMemberEmail());
        //유저 인증정보 검증
        if (!validResult) {
            return ResponseEntity.badRequest().body("로그인 정보가 없습니다.");
        }

        //리뷰 삭제, 성공시 true, 실패시 예외로 던집니다.
        boolean result = reviewService.deleteReview(reviewDeleteDto);

        //리뷰 삭제 성공시 삭제완료 메시지 전송
        if (result) {
            log.info("리뷰 삭제 요청 성공");
            return ResponseEntity.ok("리뷰 삭제 완료");
        } else {
            log.error("리뷰 삭제 요청 실패, 관리자 확인 필요");
            return ResponseEntity.internalServerError().body("서버에러 관리자에게 문의해주세요");
        }

    }


}

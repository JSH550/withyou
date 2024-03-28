package com.js.withyou.controller;

import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.dto.MemberCreateDto;
import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.dto.MemberNameDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.review.ReviewMypageDto;
import com.js.withyou.service.MemberLikePlaceService;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import com.js.withyou.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
//@RequestMapping()
public class MemberController {
    private final MemberService memberService;
    private final MemberLikePlaceService memberLikePlaceService;

    private final PasswordEncoder passwordEncoder;


    private final ReviewService reviewService;

//    private final PlaceService placeService;

    @Autowired
    public MemberController(MemberService memberService, PlaceService placeService, MemberLikePlaceService memberLikePlaceService, PasswordEncoder passwordEncoder, ReviewService reviewService) {
        this.memberService = memberService;
//        this.placeService = placeService;
        this.memberLikePlaceService = memberLikePlaceService;
        this.passwordEncoder = passwordEncoder;
        this.reviewService = reviewService;
    }

//    @GetMapping(value = "")
//    public String showMemberHome() {
//
//        return "/member/memberHome";
//    }

    @GetMapping(value = "/signup")
    public String showSignup(Model model) {
        model.addAttribute("memberCreateDto", new MemberCreateDto());
        return "/member/signup";
    }

    ;

    //회원가입
    @PostMapping("/signup")
    public String addNewMember(@Validated @ModelAttribute("memberDto") MemberCreateDto memberCreateDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("errors ={}", bindingResult);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/member/signup";
        } else {
            MemberDto savedMember = memberService.saveMember(memberCreateDto);
            return "redirect:/mypage";
        }
//        log.info("MemberPassWord={}", memberDto.getMemberPassword());
//        log.info("MemberEmail={}", memberDto.getMemberEmail());
//        log.info("MemberName={}", memberDto.getMemberName());


    }

    //    로그인
    @GetMapping("/login")
    public String showLoginPage() {
//        return "/member/loginForm";
        return "/member/login";
    }


    //Spring security 사용으로 더이상 사용하지 않는 메서드 입니다.
//    @PostMapping("/login")
//    public String login(@RequestParam String memberEmail,
//                        @RequestParam String password,
//                        @RequestParam(required = false, defaultValue = "/") String redirectURL,
//                        HttpServletRequest request) {
//        log.info("redirectURL={}", redirectURL);
//
//
//        Optional<MemberDto> loginMember = memberService.login(memberEmail, password);
//        if (loginMember.isEmpty() == true) {
//            log.info("로그인 에러 검출");
//            return "/member/loginForm";
//        } else {
//
//        }
//        //세션+쿠키(토큰) 방식으로 로그인 유저 저장
//        //getSession(true) 세션이 없으면 만들고 있으면 반환
//        //getSession(false) 세션이 없으면 새로만들지 않음
//        HttpSession session = request.getSession(true);
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute("LOGIN_MEMBER", loginMember.get().getMemberName());
//        log.info("이메일정보확인", loginMember.get().getMemberEmail());
//        session.setAttribute("LOGIN_MEMBER_EMAIL", loginMember.get().getMemberEmail());
//
//        log.info("Session LOGIN_MEMBER: {}", session.getAttribute("LOGIN_MEMBER"));
//        log.info("Session LOGIN_MEMBER_EMAIL: {}", session.getAttribute("LOGIN_MEMBER_EMAIL"));
//        return "redirect:/mypage";
////        return "redirect:" + redirectURL;
//
//    }


    ;

//    @ResponseBody
//    @GetMapping("/mypage")
////    public String mypage(@SessionAttribute(name = "LOGIN_MEMBER", required = false) String memberName,
////                         @SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail
////            , HttpServletRequest request,
////                         Model model) {
//        log.info("로그인한 유저 닉네임 ={}",customUser.memberName);
////        HttpSession session = request.getSession(false);
//        if (authentication.isAuthenticated()) {
//            log.info("로그인한 유저={}",authentication.getName());
//            return "ok";
//        } else {
//            log.info("로그인 하지 않은 사용자 login page로 이동합니다.");
//            return "redirect:/login";
////            log.info("login member, mypage로 redirect");
////            log.info(String.valueOf(memberName));
//            //session 정보를 DTO에 담아서 전송합니다, DTO에는 유저 이름과 이메일만 담아서 전송합니다.
////            MemberNameDto memberNameDto = new MemberNameDto();
////            memberNameDto.setMemberName(memberName);
////            memberNameDto.setMemberEmail(memberEmail);
////            model.addAttribute("memberDto", memberNameDto);
//
//        }

    @GetMapping("/mypage")
    public String showMypage(Authentication authentication,
                             Model model) {
        //유저 정보를 가져옵니다.
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        log.info("로그인한 유저 닉네임 ={}", customUser.memberName);
        if (authentication.isAuthenticated()) {
            log.info("로그인한 유저={}", authentication.getName());
            return "member/mypage";
        } else {
            log.info("로그인 하지 않은 사용자 login page로 이동합니다.");
            return "redirect:/login";
        }
    }




    /**
     * 유저가 본인이 좋아요 누른 시설을 보여주는 메서드 입니다.
     *
     * @param memberEmail
     * @param model
     * @return 좋아요 누른 시설(place)를 보여주는 view페이지를 반환합니다.
     */
    @GetMapping("/mypage/likes/places")
    public String showMemberLikePlace(@SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail,
                                      Authentication authentication,
                                      Model model) {

        //유저 정보를 가져옵니다.
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        List<PlaceDto> placeDtoList = memberLikePlaceService.getPlaceDtosByMemberEmail(customUser.getUsername());
        model.addAttribute("placeDtoList", placeDtoList);
        return "/member/member-like-places";
    }

    @GetMapping("/mypage/reviews")
    public String showReviewsByMember(@SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail,
                                      Model model) {
        //멤버가 작성한 리뷰들 찾아서 가져와주세요~
        List<ReviewMypageDto> reviewsByMemberEmail = reviewService.getReviewsByMemberEmail(memberEmail);
        model.addAttribute("reviews", reviewsByMemberEmail);
        return "/member/member-reviews";
    }

    @ResponseBody
    @GetMapping("/test52")
    public String test52(Authentication authentication){
//
//        CustomUser customUser =  (CustomUser) SecurityContextHolder.getContext().getAuthentication();
//        Object principal = customUser.toString();

//        log.info("유저정보={}",principal.toString());
        return "ok";

    }

    ;

    public boolean memberEmptyCheck(String memberId) {
        Optional<MemberDto> memberByName = Optional.ofNullable(memberService.getMemberDtoByName(memberId));
        if (memberByName.isEmpty() == true) {
            return true;
        } else {
            return false;
        }
    }

//    @GetMapping("/logout")
//    public String lougOut(HttpServletResponse response){
//        Cookie cookie = deleteCookie("memberId");
//        response.addCookie(cookie);
//        return "redirect:/members";
//    }

    @GetMapping("/logout")
    public String lougOut(HttpServletRequest request,
                          HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            //session 정보 삭제
            session.invalidate();
        }

        return "redirect:/members";
    }


    @GetMapping("/members/password/change")
    private String showPasswordChangePage() {
        //뷰 페이지를 보여줍니다.
        return "/member-password-change";
    }

    public Cookie deleteCookie(String cookieName) {
        //cookie age를 0으로 바꿔 삭제
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        return cookie;
    }
}

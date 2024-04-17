package com.js.withyou.controller;

import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.dto.member.MemberCreateDto;
import com.js.withyou.data.dto.member.MemberDto;
import com.js.withyou.data.dto.member.MemberNameDto;
import com.js.withyou.data.dto.member.MemberPasswordDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.review.ReviewMypageDto;
import com.js.withyou.service.MemberLikePlaceService;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
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


    /**
     * 유저의 닉네임을 바꾸는 API입니다.
     *
     * @param memberNameDto  email과 닉네임을 담는 DTO 입니다.
     * @param authentication spring security 인증정보입니다.
     * @return ResponseEntity
     */
    @ResponseBody
    @PatchMapping("/members/name")
    public ResponseEntity changeMemberNickname(@RequestBody MemberNameDto memberNameDto,
                                               Authentication authentication) {

        log.info("요청자={}", authentication.toString());
        log.info("인증여부={}", authentication.isAuthenticated());

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인 정보가 없습니다.");
        }
        //인증 정보를 파싱합니다.
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        //로그인 인증 유저와 닉네임 변경 유저가 동일한지 확인
        if (memberNameDto.getMemberEmail().equals(customUser.getUsername())) {
            log.info("닉네임 변경 요청 유저 = {}", memberNameDto.getMemberEmail());
            boolean result = memberService.updateMemberName(memberNameDto);
            log.info("닉네임 변경 요청 결과={}", result);
            customUser.memberName = memberNameDto.getMemberName();

            // 새로운 Authentication 객체 생성합니다. 토큰은 UsernamePasswordAuthenticationToken
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(customUser
                    , authentication.getCredentials() // 현재 인증에 사용된 자격 증명 저장
                    , customUser.getAuthorities());// 현재 사용자의 권한 목록을 저장
            // SecurityContextHolder에서 현재 인증 정보를 설정합니다.
            SecurityContextHolder.getContext().setAuthentication(newAuthentication);

            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.badRequest().body("계정 정보가 없습니다.");

        }


        //닉넴 변경 요청 받아서
        //로그인한 유저랑 동일한지 확인하고
        //맞으면 비즈니스로직 불러서 해결좀

    }

    /**
     * 비밀번호 변경 페이지를 보여주는 API 입니다.
     *
     * @return
     */

    @GetMapping("/members/password")
    public String showChangePasswordForm() {
        return "/member/member-password-change";
    }

    /**
     * 비밀번호를 변경하는 API 입니다.
     *
     * @param authentication    사용자 인증 정보를 확인하니다.
     * @param memberPasswordDto 유저의 email과 변경할 password를 전달받습니다.
     * @return
     */





    @ResponseBody
    @PatchMapping("/members/password")
    public ResponseEntity changeMemberPassword(Authentication authentication,
                                               @RequestBody MemberPasswordDto memberPasswordDto) {

        //사용하지 않는 코드 service 레이어로 이동
//        log.info("요청자={}", authentication.toString());
//        log.info("인증여부={}", authentication.isAuthenticated());
////        log.info("전송된값={}", memberPasswordDto.toString());
//        if (!authentication.isAuthenticated()) {
//            log.info("로그인 정보 에러, 사용자 정보 없음");
//            return ResponseEntity.badRequest().body("로그인 정보가 없습니다.");
//        }

        boolean validResult = memberService.validateUserAuthentication(authentication, memberPasswordDto.getMemberEmail());

        if(validResult) {
            log.info("비밀번호 변경 요청 유저 = {}, 인증결과 = {}", memberPasswordDto.getMemberEmail(),validResult);
            boolean result = memberService.updateMemberPassword(memberPasswordDto);//DB에서 유저의 PW 변경
            log.info("비밀번호 변경 요청 결과={}", result);
            if (result) {
                return ResponseEntity.ok().body("ok");
            }
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.badRequest().body("요청에러");


    }




    @ResponseBody
    @GetMapping("/test52")
    public String test52(Authentication authentication) {
//
//        CustomUser customUser =  (CustomUser) SecurityContextHolder.getContext().getAuthentication();
//        Object principal = customUser.toString();

//        log.info("유저정보={}",principal.toString());
        return "ok";
    }


    //spring securtiy session 변경 테스트입니다.
    @ResponseBody
    @GetMapping("/testU")
    public String sessionUpdateTest(Authentication authentication) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        customUser.memberName = "테스트닉네임";

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(customUser, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        return "ok";


    }

    @ResponseBody
    @GetMapping("/vaildTest")
    public ResponseEntity validateUserAuthenticationTest(Authentication authentication){
        String email = "tes2t@gmail.com";
        boolean validResult = memberService.validateUserAuthentication(authentication,email);
        if (validResult){
        return ResponseEntity.ok("OK");
        };
        return ResponseEntity.badRequest().body("서버에러");

    };

    @ResponseBody
    @GetMapping("/testO")
    public String sessionOutputTest(Authentication authentication,
                                    HttpServletRequest request) {

        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getSession().getAttribute(attributeName);
            log.info("Attribute Name: " + attributeName + ", Value: " + attributeValue);
        }

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        log.info("CustomUser = {}",customUser.memberName);
        log.info("Authentication Details:");
        log.info("Principal: {}", authentication.getPrincipal());
        log.info("Authorities: {}", authentication.getAuthorities());
        log.info("Credentials: {}", authentication.getCredentials());
        log.info("Is Authenticated: {}", authentication.isAuthenticated());
        log.info("Name: {}", authentication.getName());
        log.info("Details", authentication.getDetails());

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

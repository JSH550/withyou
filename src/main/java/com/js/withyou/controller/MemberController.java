package com.js.withyou.controller;

import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.dto.MemberNameDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.service.MemberLikePlaceService;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

//    private final PlaceService placeService;

    @Autowired
    public MemberController(MemberService memberService, PlaceService placeService, MemberLikePlaceService memberLikePlaceService) {
        this.memberService = memberService;
//        this.placeService = placeService;
        this.memberLikePlaceService = memberLikePlaceService;
    }

    @GetMapping(value = "")
    public String showMemberHome() {

        return "/member/memberHome";
    }

    @GetMapping(value = "/signup")
    public String showSignup(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "/member/signup";
    }

    ;

    //회원가입
    @PostMapping("")
    public String addNewMember(@Validated @ModelAttribute("memberDto") MemberDto memberDto,
                               BindingResult bindingResult,
                               Model model,
                               HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            log.info("errors ={}", bindingResult);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "/member/signup";
        } else {
            MemberDto savedMember = memberService.saveMember(memberDto);
            return "redirect:/members";
        }
//        log.info("MemberPassWord={}", memberDto.getMemberPassword());
//        log.info("MemberEmail={}", memberDto.getMemberEmail());
//        log.info("MemberName={}", memberDto.getMemberName());


    }

    //    로그인
    @GetMapping("/login")
    public String showLoginPage() {
        return "/member/loginForm";
    }


    @PostMapping("/login")
    public String login(@RequestParam String memberEmail,
                        @RequestParam String password,
                        @RequestParam(required = false, defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {
        log.info("redirectURL={}", redirectURL);


        Optional<MemberDto> loginMember = memberService.login(memberEmail, password);
        if (loginMember.isEmpty() == true) {
            log.info("로그인 에러 검출");
            return "/member/loginForm";
        } else {

        }
        //세션+쿠키(토큰) 방식으로 로그인 유저 저장
        //getSession(true) 세션이 없으면 만들고 있으면 반환
        //getSession(false) 세션이 없으면 새로만들지 않음
        HttpSession session = request.getSession(true);
        //세션에 로그인 회원 정보 보관
        session.setAttribute("LOGIN_MEMBER", loginMember.get().getMemberName());
        log.info("이메일정보확인", loginMember.get().getMemberEmail());
        session.setAttribute("LOGIN_MEMBER_EMAIL", loginMember.get().getMemberEmail());

        log.info("Session LOGIN_MEMBER: {}", session.getAttribute("LOGIN_MEMBER"));
        log.info("Session LOGIN_MEMBER_EMAIL: {}", session.getAttribute("LOGIN_MEMBER_EMAIL"));
        return "redirect:/mypage";
//        return "redirect:" + redirectURL;

    }

    ;

    /**
     * @param memberName
     * @param memberEmail
     * @param request
     * @param model
     * @return
     * @ToDo 시설 즐겨찾기 기능
     * @Todo 내가쓴 댓글 보기 기능
     */
    //(@SessionAttribute(name ="LOGIN_MEMBER",required = false session에 있는지 확인, 새로생성하진 않음
    @GetMapping("/mypage")
    public String mypage(@SessionAttribute(name = "LOGIN_MEMBER", required = false) String memberName,
                         @SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail
            , HttpServletRequest request,
                         Model model) {

//        HttpSession session = request.getSession(false);
        if (memberName == null) {
            log.info("로그인 하지 않은 사용자 login page로 redirect");
            return "redirect:/login";
        } else {

            log.info("login member, mypage로 redirect");
            log.info(String.valueOf(memberName));

            //session 정보를 DTO에 담아서 전송합니다, DTO에는 유저 이름과 이메일만 담아서 전송합니다.
            MemberNameDto memberNameDto = new MemberNameDto();
            memberNameDto.setMemberName(memberName);
            memberNameDto.setMemberEmail(memberEmail);

            model.addAttribute("memberDto", memberNameDto);

            return "/member/memberMyPage";
        }


//        memberId.equals(null) 값이 null일경우 equals호출할수 없음 Objects.equlas(memberId,null)이렇게 하자
        /*
        mypage 접속시 cookie 확인, 없거나 DB에서 조회가 안되면 로그인 페이지로 리다이렉트
         */
//        if ( Objects.equals(memberId, null)){
//            log.info("로그인 하지 않은 사용자");
//        return "redirect:/members/login";
//       }else if(memberEmptyCheck(memberId)) {
//            log.info("등록되지 않은 사용자 접속");
//            return "redirect:/members/login";
//        }else {
//            model.addAttribute("memberId",memberId);
//            return "/member/memberMyPage";
//        }


    }

    @GetMapping("/mypage/likes/places")
    public String showMemberLikePlace(@SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail,
                                       Model model) {
        List<PlaceDto> placeDtoList = memberLikePlaceService.getPlaceDtosByMemberEmail(memberEmail);
        model.addAttribute("placeDtoList", placeDtoList);
        return "/member/member-like-places";
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

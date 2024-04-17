package com.js.withyou.service;

import com.js.withyou.data.dto.member.MemberCreateDto;
import com.js.withyou.data.dto.member.MemberDto;
import com.js.withyou.data.dto.member.MemberNameDto;
import com.js.withyou.data.dto.member.MemberPasswordDto;
import com.js.withyou.data.entity.Member;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface MemberService {
    MemberDto saveMember(MemberCreateDto memberCreateDto);

    MemberDto getMemberDtoById(Long memberId);

    MemberDto getMemberDtoByName(String memberName);

    Member getMemberEntityByEmail(String email);

    Optional<MemberDto> login(String memberName, String password);

    /**
     * 유저의 닉네임을 업데이트하는 메서드입니다.
     * @param memberNameDto 유저의 email과 변경할 닉네임을 전달받습니다.
     * @return
     */
    boolean updateMemberName(MemberNameDto memberNameDto);

    /**
     * 유저의 비밀번호를 업데이트하는 메서드입니다.
     *
     * @param memberPasswordDto 유저의 email과 변경할 password를 담고 있습니다.
     * @return
     */
    boolean updateMemberPassword(MemberPasswordDto memberPasswordDto);


    /**
     * 유저가 입력한 비밀번호와 DB에서 유저의 비밀번호가 같은지 확인하는 메서드입니다.
     * @param password
     * @return
     */
    boolean confirmPasswordMatch(String memberEmail, String password);

    /**
     * 유저 인증정보를 검증하는 메서드입니다.
     * @param authentication API를 호출한 유저의 인증 정보를 받습니다.
     * @param memberEmail 클라이언트에서 API를 호출한 유저 Email을 받습니다.
     * @return boolean 인증 결과를 반환합니다.
     */
   boolean validateUserAuthentication(Authentication authentication, String memberEmail);

//    boolean validateUserAuthentiikcation(Authentication authentication, String memberEmail);
}

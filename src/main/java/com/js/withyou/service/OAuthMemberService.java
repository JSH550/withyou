package com.js.withyou.service;

import com.js.withyou.domain.dto.member.MemberNameDto;
import com.js.withyou.utils.OAuthProvider;

public interface OAuthMemberService {


    /**
     * OAuth 유저의 정보를 DB에 저장하는 메서드 입니다.
     * @param memberEmail 유저 email
     * @param memberName 유저 닉네임
     * @param memberRole 역할
     * @param provider 서비스 제공자입니다.(google,naver등)
     * @param providerId 서비스 제공자에서의 유저 ID 값입니다.
     */
    void saveOAuthMember(String memberEmail, String memberName, String memberRole, OAuthProvider provider, String providerId);


    /**
     * DB에서 이메일로 유저를 검색합니다.
     * 검색 결과가 있을경우, memberNameDto 로 변환후 반환, 검색결과가 없을경우 빈객체 반환
     * @param memberEmail
     * @return MemberNameDto
     */
    MemberNameDto findOAuthMemberByEmail(String memberEmail);



}

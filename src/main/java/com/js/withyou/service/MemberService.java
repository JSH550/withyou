package com.js.withyou.service;

import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.entity.Member;

import java.util.Optional;

public interface MemberService {
    MemberDto saveMember(MemberDto memberDto);

    MemberDto getMemberDtoById(Long memberId);

    MemberDto getMemberDtoByName(String memberName);

    Member getMemberEntityByEmail(String email);

    Optional<MemberDto> login(String memberName, String password);
}

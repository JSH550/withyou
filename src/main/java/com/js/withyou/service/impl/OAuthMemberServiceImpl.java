package com.js.withyou.service.impl;

import com.js.withyou.domain.dto.member.MemberNameDto;
import com.js.withyou.domain.entity.Member;
import com.js.withyou.repository.MemberRepository;
import com.js.withyou.service.OAuthMemberService;
import com.js.withyou.utils.OAuthProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OAuthMemberServiceImpl implements OAuthMemberService {

    private final MemberRepository memberRepository;

    public OAuthMemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void saveOAuthMember(String memberEmail, String memberName, String memberRole, OAuthProvider provider, String providerId) {

        Member member = new Member();

        //저장될 providerId 재정의, provider_provderId 값으로 저장합니다(중복방지)

        String savedProviderId = provider +"_" + providerId;


        //DB에 레코드 저장
        member.saveOAuthMember(
                memberEmail,
                memberName,
                memberRole,
                provider,
                savedProviderId);
        //DB에 레코드 저장
        memberRepository.save(member);

    }

    @Override
    public MemberNameDto findOAuthMemberByEmail(String memberEmail) {

        Optional<Member> foundMember = memberRepository.findByMemberEmail(memberEmail);

        MemberNameDto memberNameDto = new MemberNameDto();

        //검색 결과가 있을경우, memberNameDto 로 변환후 반환 없을경우 빈객체 반환
        if (foundMember.isPresent()) {
            return memberNameDto.convertToMemberNameDto(foundMember.get());
        } else {
            return memberNameDto;
        }


    }


}

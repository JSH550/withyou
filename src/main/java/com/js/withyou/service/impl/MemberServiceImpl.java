package com.js.withyou.service.impl;

import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.repository.MemberRepository;
import com.js.withyou.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDto saveMember(MemberDto memberDto) {
        Member member = new Member();
        member.saveMember(
                memberDto.getMemberEmail(),
                memberDto.getMemberName(),
                memberDto.getMemberPassword());
//                memberDto.getMemberEmail(),
//                memberDto.getMemberNickName());
        Member savedMember = memberRepository.save(member);

        return convertToDto(savedMember);
    }

    @Override
    public MemberDto getMemberDtoById(Long memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        if(foundMember.isPresent()){
            Member foundMemberEntity = foundMember.get();
            return convertToDto(foundMemberEntity);
        }else{

            throw new NoSuchElementException("Member not found with ID: " + memberId);
        }
    }

    @Override
    public MemberDto getMemberDtoByName(String memberName) {

        Optional<Member> foundMemberByName = memberRepository.findByMemberName(memberName);
        if(foundMemberByName.isPresent()){
            return convertToDto(foundMemberByName.get());
        }else{
            throw new NoSuchElementException("Member not found with memberName: " + memberName);
        }
    }

    /**
     * Email로 유저 정보를 조회하는 메서드입니다.
     * @param email 조회할 계정의 Email 입니다.
     * @return Member entity 객체입니다. 컨트롤러로 전송하지 않게 주의합니다.(DTO를 반환하는 메서드를 이용해주세요)
     */
    @Override
    public Member getMemberEntityByEmail(String email) {
        Optional<Member> foundMember = memberRepository.findByMemberEmail(email);
        if (foundMember.isPresent()){
            return foundMember.get();
        }else {
            throw new IllegalArgumentException("이메일 검색을 통한 유저 정보가 존재하지 않습니다.");
        }
    }

    ;




    @Override
    public Optional<MemberDto> login(String memberEmail, String password) {
        try {
            //memberRepository에 의해서 DB에서 찾아진 값이 Optional<Member>로 꺼내진다
//            Member member = memberRepository.findByMemberName(memberName)
            Member member = memberRepository.findByMemberEmail(memberEmail)
                    .filter(foundMember -> foundMember.getMemberPassword().equals(password))
                    .orElseThrow(NoSuchFieldException::new);
            return Optional.of(convertToDto(member));
        } catch (NoSuchFieldException e) {
            log.info("로그인에러");
            ;
        }
        return Optional.empty();
    };



    private MemberDto convertToDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setMemberName(member.getMemberName());
        memberDto.setMemberPassword(member.getMemberPassword());
        memberDto.setMemberEmail(member.getMemberEmail());
//        memberDto.setMemberNickName(member.getMemberNickName());
        return memberDto;
    }
}

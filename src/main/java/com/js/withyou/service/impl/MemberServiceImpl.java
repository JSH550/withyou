package com.js.withyou.service.impl;

import com.js.withyou.data.dto.member.MemberCreateDto;
import com.js.withyou.data.dto.member.MemberDto;
import com.js.withyou.data.dto.member.MemberNameDto;
import com.js.withyou.data.dto.member.MemberPasswordDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.repository.MemberRepository;
import com.js.withyou.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MemberDto saveMember(MemberCreateDto memberCreateDto) {
        String encodedPassword = passwordEncoder.encode(memberCreateDto.getMemberPassword());//비밀번호를 인코딩합니다.
//        Member member = Member.builder()
//                .memberEmail(memberCreateDto.getMemberEmail())
//                .memberPassword(encodedPassword)
//                .memberName(memberCreateDto.getMemberName())
//                .build();

        Member member = new Member();
        member.saveMember(
                memberCreateDto.getMemberEmail(),
                memberCreateDto.getMemberName(),
                encodedPassword);

        Member savedMember = memberRepository.save(member);

        return convertToDto(savedMember);
    }

    @Override
    public MemberDto getMemberDtoById(Long memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        if (foundMember.isPresent()) {
            Member foundMemberEntity = foundMember.get();
            return convertToDto(foundMemberEntity);
        } else {

            throw new NoSuchElementException("Member not found with ID: " + memberId);
        }
    }

    @Override
    public MemberDto getMemberDtoByName(String memberName) {

        Optional<Member> foundMemberByName = memberRepository.findByMemberName(memberName);
        if (foundMemberByName.isPresent()) {
            return convertToDto(foundMemberByName.get());
        } else {
            throw new NoSuchElementException("Member not found with memberName: " + memberName);
        }
    }

    /**
     * Email로 유저 정보를 조회하는 메서드입니다.
     *
     * @param email 조회할 계정의 Email 입니다.
     * @return Member entity 객체입니다. 컨트롤러로 전송하지 않게 주의합니다.(DTO를 반환하는 메서드를 이용해주세요)
     */
    @Override
    public Member getMemberEntityByEmail(String email) {
        Optional<Member> foundMember = memberRepository.findByMemberEmail(email);
        if (foundMember.isPresent()) {
            return foundMember.get();
        } else {
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
    }

    @Transactional
    @Override
    public boolean updateMemberName(MemberNameDto memberNameDto) {

        Optional<Member> foundMember = memberRepository.findByMemberEmail(memberNameDto.getMemberEmail());

        boolean saveResult = false;//결과를 반환할 불리안 객체입니다. default값은 false입니다.
        //요청한 유저의 email 검색 결과가 존재하고, 변경하고자 하는 닉네임이 현재의 닉네임과 다른지 확인합니다.
        if (foundMember.isPresent() && !foundMember.get().getMemberName().equals(memberNameDto.getMemberName())) {
            Member member = foundMember.get();
            member.updateMemberName(memberNameDto.getMemberName());
            Member updatedMember = memberRepository.save(member);
            saveResult = updatedMember.getMemberEmail().equals(memberNameDto.getMemberEmail());//update 결과를 저장합니다.
        }
        return saveResult;

    }

    @Transactional
    @Override
    public boolean updateMemberPassword(MemberPasswordDto memberPasswordDto) {
        boolean saveResult = false;//결과를 반환할 불리안 객체입니다. default값은 false입니다.

            Optional<Member> foundMember = memberRepository.findByMemberEmail(memberPasswordDto.getMemberEmail());
            if (foundMember.isEmpty()) {
                log.info("패스워드 변경 요청 에러, 저장되지 않은 사용자");
                throw new EntityNotFoundException("로그인 정보를 확인해 주세요");
//            return saveResult = false;
            }

            String encodedNewPassword = passwordEncoder.encode(memberPasswordDto.getMemberPassword());//새로운 비밀번호를 인코딩합니다.
            //새로운 비밀번호가 DB에 저장된 비밀번호와 같은지 확인합니다, 인코딩 되어있으므로 passwordEncoder.matches 메서드를 이용합니다.
            if (passwordEncoder.matches(memberPasswordDto.getMemberPassword(), foundMember.get().getMemberPassword())) {
                log.info("패스워드 변경 요청 에러, 기존 패스워드와 동일한 패스워드");
                throw new DataIntegrityViolationException("패스워드 변경 요청 에러,기존과 동일한 패스워드 입니다. ");
//            return saveResult = false;
            }
            ;
//
//        if (encodedNewPassword.equals(foundMember.get().getMemberPassword())) {
//            log.info("패스워드 변경 요청 에러, 기존 패스워드와 동일한 패스워드");
//            return saveResult = false;
//        }

            Member member = foundMember.get();//DB에서 찾아온 레코드 값을 객체에 저장합니다.
            member.updateMemberPassword(encodedNewPassword);//비밀번호를 새로운 비밀번호로 설정합니다.
            Member updatedMember = memberRepository.save(member);

            //요청한 유저와 패스워드를 변경한 유저가 동일한지 확인합니다.
            if (member.getMemberId() != updatedMember.getMemberId()) {
                log.info("패스워드 변경 요청 에러, DB 저장 에러");

                throw new DataIntegrityViolationException("패스워드 변경 요청 에러. 관리자에게 문의하세요 ");

//                saveResult = true;
            }

            return saveResult = true;


    }

    ;


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

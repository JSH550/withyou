package com.js.withyou.service.impl;

import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.dto.member.MemberCreateDto;
import com.js.withyou.data.dto.member.MemberDto;
import com.js.withyou.data.dto.member.MemberNameDto;
import com.js.withyou.data.dto.member.MemberPasswordDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.repository.MemberRepository;
import com.js.withyou.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

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
        Optional<Member> foundMember = memberRepository.findByMemberEmail(memberPasswordDto.getMemberEmail());
        String newPassword = memberPasswordDto.getNewPassword();

        //유저가 입력한  현재 비밀번호가 맞는지 확인합니다.
        boolean passwordMatch = confirmPasswordMatch(memberPasswordDto.getMemberEmail(), memberPasswordDto.getOldPassword());
        if (!passwordMatch) {
            log.error("패스워드 변경 요청 에러, 입력한 현재 비밀번호가 올바르지 않습니다.");
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        Member requestMember = foundMember.get();//Optional 을 제거합니다.

        //새로운 비밀번호가 DB에 저장된 비밀번호와 같은지 확인합니다, 인코딩 되어있으므로 passwordEncoder.matches 메서드를 이용합니다.
        if (passwordEncoder.matches(memberPasswordDto.getNewPassword(), requestMember.getMemberPassword())) {
            log.error("패스워드 변경 요청 에러, 기존 패스워드와 동일한 패스워드");
            throw new IllegalArgumentException("기존 패스워드와 동일한 패스워드입니다.");
        }

        //새로운 비밀번호가 조건에 부합하는지 확인합니다.
        boolean validateResult = validatePassword(newPassword);

        log.info("비밀번호 검증결과 = {}",String.valueOf(validateResult));
        if (!validateResult){
            log.error("패스워드 변경 요청 에러, 기존 패스워드와 동일한 패스워드");
            throw new IllegalArgumentException("비밀번호는 8~16자의 적어도 하나의 영어 소문자, 대문자, 숫자, 특수문자가 포함되어야 합니다.  ");
        }

        String encodedNewPassword = passwordEncoder.encode(memberPasswordDto.getNewPassword());//새로운 비밀번호를 인코딩합니다.
        requestMember.updateMemberPassword(encodedNewPassword);//비밀번호를 새로운 비밀번호로 설정합니다.
        Member updatedMember = memberRepository.save(requestMember);

        // 업데이트된 멤버가 null이 아닌지 확인하여 저장이 제대로 이루어졌는지 확인합니다.
        if (updatedMember == null) {
            log.error("패스워드 변경 요청 에러, DB 저장 에러");
            throw new DataIntegrityViolationException("패스워드 변경 요청 에러. 관리자에게 문의하세요 ");
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean confirmPasswordMatch(String memberEmail, String password) {
        Optional<Member> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isEmpty()) {
            log.info("패스워드 확인 요청 에러 발생, 저장되지 않은 사용자");
            return false; // 사용자가 존재하지 않는 경우 false 반환
        }

        Member member = byMemberEmail.get();

        if (passwordEncoder.matches(password,member.getMemberPassword())) {
            return true;
        } else {
            log.info("패스워드 확인 요청 에러 발생, 입력한 비밀번호가 맞지 않음");
            return false;
        }
    }


    /**
     * 유저 인증정보를 검증하는 메서드입니다.
     * @param authentication API를 호출한 유저의 인증 정보를 받습니다.
     * @param memberEmail 클라이언트에서 API를 호출한 유저 Email을 받습니다.
     * @return boolean 인증 결과를 반환합니다.
     */
    @Override
    public boolean validateUserAuthentication(Authentication authentication, String memberEmail) {
        if (authentication==null) {
            throw  new BadCredentialsException("사용자 인증정보 에러, 사용자 인증 정보 없음");
        } else if (!authentication.isAuthenticated()) {
            throw  new BadCredentialsException("사용자 인증정보 에러, 사용자 인증 정보 없음");
        }
        //인증 정보를 파싱합니다. 현재 프로젝트에서는 CustomUser class를 만들어 사용중입니다.
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        log.info("파싱한정보={}",customUser);
        //로그인 인증 유저와 닉네임 변경 유저가 동일한지 검증
        if (!memberEmail.equals(customUser.getUsername())){
            throw  new BadCredentialsException("로그인 정보가 올바르지 않습니다.");
        }
        return true;
    }

    ;

    public boolean validatePassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$#!%*?&]{8,16}$";
        return Pattern.matches(regex, password);
    }


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

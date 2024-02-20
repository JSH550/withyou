package com.js.withyou.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MemberDto {
    private Long memberId;
    @Email(message = "올바른 이메일 형식이어야 합니다.")

    @NotBlank(message = "이메일을 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String memberPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=2, max=16, message = "닉네임은 2~16글자 사이로 지어주세요 ")
    private String memberName;


}

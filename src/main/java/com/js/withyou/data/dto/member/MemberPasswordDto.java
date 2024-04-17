package com.js.withyou.data.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString


public class MemberPasswordDto {

    private String memberEmail;

    private String newPassword;

    private String oldPassword;



}

package com.js.withyou.data.dto.member;

import com.js.withyou.data.entity.Member;
import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberNameDto {
    /**
     * 사용자의 이름(닉네임), email만 전달하는 Dto 입니다.
     */

    private String memberEmail;

    private String memberName;
//    public MemberNameDto convertToMemberNameDto(Member member){
//        MemberNameDto memberNameDto = new MemberNameDto();
//        memberNameDto.setMemberEmail(member.getMemberEmail());
//        memberNameDto.setMemberName(member.getMemberName());
//        return memberNameDto;
//    }
//

}

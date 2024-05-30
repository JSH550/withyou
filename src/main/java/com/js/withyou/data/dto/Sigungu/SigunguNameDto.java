package com.js.withyou.data.dto.Sigungu;

import com.js.withyou.data.entity.Region.Sigungu;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

//sigungu의 이름만 전송하는 DTO입니다.
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigunguNameDto {

    private Long id;
    @NotBlank(message = "세부지역 이름 정보 없음")
    private String sigunguName;


    public SigunguNameDto convertToSigunguNameDto(Sigungu sigungu) {
   return   SigunguNameDto.builder()
                .id(sigungu.getSigunguId())
                .sigunguName(sigungu.getSigunguName())
                .build();

    }

    public SigunguNameDto convertToSigunguNameDto(String sigunguName) {
        SigunguNameDto sigunguNameDto = new SigunguNameDto();
        sigunguNameDto.setSigunguName(sigunguName);
        return sigunguNameDto;
    }


}

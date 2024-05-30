package com.js.withyou.data.dto.Sigungu;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigunguSaveDto {

    @NotBlank(message = "세부지역 이름 정보 없음")
    private String sigunguName; //
    @NotBlank(message = "지역 이름 정보 없음")
    private Long sidoId;

}

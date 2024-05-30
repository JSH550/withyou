package com.js.withyou.data.dto.Sigungu;

import jakarta.validation.constraints.NotBlank;

public class SigunguCreateDto {
    
    @NotBlank(message = "세부지역 이름을입력해주세요")
    private String sigunguName; //
}

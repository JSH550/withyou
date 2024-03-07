package com.js.withyou.data.dto.SubRegion;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubRegionSaveDto {

    @NotBlank(message = "세부지역 이름 정보 없음")
    private String subRegionName; //
    @NotBlank(message = "지역 이름 정보 없음")
    private Long regionId;

}

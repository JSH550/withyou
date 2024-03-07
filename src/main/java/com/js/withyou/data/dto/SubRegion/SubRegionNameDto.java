package com.js.withyou.data.dto.SubRegion;

import com.js.withyou.data.entity.SubRegion;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//subRegion의 이름만 전송하는 DTO입니다.
@Getter
@Setter
@ToString
public class SubRegionNameDto {

    @NotBlank(message = "세부지역 이름 정보 없음")
    private String subRegionName;


    public SubRegionNameDto convertToSubRegionNameDto(SubRegion subregion) {
        SubRegionNameDto subRegionNameDto = new SubRegionNameDto();
        subRegionNameDto.setSubRegionName(subregion.getSubRegionName());
        return subRegionNameDto;
    }

    public SubRegionNameDto convertToSubRegionNameDto(String subRegionName) {
        SubRegionNameDto subRegionNameDto = new SubRegionNameDto();
        subRegionNameDto.setSubRegionName(subRegionName);
        return subRegionNameDto;
    }
}

package com.js.withyou.data.dto.Region;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class RegionDto {

    @NotBlank
    private Long regionId;


    @NotBlank
    private String regionName;

    private String regionShortName; // 시도명의 줄임말 필드,ex) 충남 충북

}

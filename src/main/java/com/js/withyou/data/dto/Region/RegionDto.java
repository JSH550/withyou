package com.js.withyou.data.dto.Region;

import com.js.withyou.data.entity.SubRegion;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class RegionDto {

    @NotBlank
    private Long regionId;


    @NotBlank
    private String regionName;

    private String regionShortName; // 시도명의 줄임말 필드,ex) 충남 충북


    private List<SubRegion> subRegions;

}

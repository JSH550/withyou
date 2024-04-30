package com.js.withyou.data.dto;

import com.js.withyou.data.entity.Region.DongRegion;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DongRegionNameDto {

    private Long dongRegionId;

    private String dongRegionName;


public DongRegionNameDto  convertToDontRegionNameDto(DongRegion dongRegion){
    return new DongRegionNameDto().builder()
            .dongRegionId(dongRegion.getDongRegionId())
            .dongRegionName(dongRegion.getDongRegionName())
            .build();
}
}

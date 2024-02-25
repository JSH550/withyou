package com.js.withyou.data.dto;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class PlaceSaveDto {

    private String placeName;

    private String placeRoadAddress;

    private double placeLatitude;//위도 Y축

    private double placeLongitude;//경도 X축

    //연관관계가 있는 entity는 primarykey만전달
    private Long categoryId; // Category 엔티티의 식별자(ID)
    private Long subRegionId; // SubRegion 엔티티의 식별자(ID)
}

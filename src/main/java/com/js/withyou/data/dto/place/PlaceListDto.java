package com.js.withyou.data.dto.place;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlaceListDto {

    private Long placeId;

    private String placeName;

    private String placeRoadAddress;

    private String categoryName; // Category 엔티티의 이름만 전달

    private String subRegionName; // SubRegion 엔티티의 이름만 전달


}

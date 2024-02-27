package com.js.withyou.data.dto.place;

import com.js.withyou.data.entity.Place;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDto {

    @NotBlank
    private Long placeId; //필수로 전달할 요소
    @NotBlank
    private String placeName; //필수로 전달할 요소

    private String placeRoadAddress;

    private double placeLatitude;//위도 Y축

    private double placeLongitude;//경도 X축

    private String categoryName; // Category 엔티티의 이름만 전달

    private String subRegionName; // SubRegion 엔티티의 이름만 전달

    public PlaceDto(){

    }

    public PlaceDto setPlaceDto (Place place){
        PlaceDto placeDto = new PlaceDto();
        placeDto.setPlaceId(place.getPlaceId());
        placeDto.setPlaceName(place.getPlaceName());
        placeDto.setPlaceRoadAddress(place.getPlaceRoadAddress());
        placeDto.setPlaceLatitude(place.getPlaceLatitude());
        placeDto.setPlaceLongitude(place.getPlaceLongitude());
        placeDto.setCategoryName(place.getCategory().getCategoryName());
        placeDto.setSubRegionName(place.getSubRegion().getSubRegionName());
        return placeDto;
    }

}

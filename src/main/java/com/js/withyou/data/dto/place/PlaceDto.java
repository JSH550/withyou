package com.js.withyou.data.dto.place;

import com.js.withyou.data.entity.Place;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;//비교하려는 두객체가 동등하면 true 반환
        if (o == null || getClass() != o.getClass()) return false;//두객체가 같은 class인지 확인
        PlaceDto placeDto= (PlaceDto) o;//타입 캐스팅
        return placeId == placeDto.placeId;//필드값 동일 유무 비교

    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId);//placeId기준으로 해시 코드 반환
    }

    public PlaceDto(){

    }

    public PlaceDto convertToPlaceDto(Place place){
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

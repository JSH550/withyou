package com.js.withyou.data.dto.place;

import com.js.withyou.data.dto.ReviewDto;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.Review;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlaceDetailDto {

    @NotBlank
    private Long placeId; //필수로 전달할 요소
    @NotBlank
    private String placeName; //필수로 전달할 요소

    private String placeRoadAddress;

    private double placeLatitude;//위도 Y축

    private double placeLongitude;//경도 X축

    private String categoryName; // Category 엔티티의 이름만 전달

    private String subRegionName; // SubRegion 엔티티의 이름만 전달

    private List<ReviewDto> reviews;


    public PlaceDetailDto convertToPlaceDetailDto(Place place) {

        PlaceDetailDto placeDetailDto = new PlaceDetailDto();

        placeDetailDto.setPlaceId(place.getPlaceId());
        placeDetailDto.setPlaceName(place.getPlaceName());
        placeDetailDto.setPlaceRoadAddress(place.getPlaceRoadAddress());
        placeDetailDto.setPlaceLatitude(place.getPlaceLatitude());
        placeDetailDto.setPlaceLongitude(place.getPlaceLongitude());
        placeDetailDto.setCategoryName(place.getCategory().getCategoryName());
        placeDetailDto.setSubRegionName(place.getSubRegion().getSubRegionName());
        List<ReviewDto> reviewDtoList = place.getReviews().stream()
                .map(review -> {
                    ReviewDto reviewDto = new ReviewDto();
                    return reviewDto.convertToReviewDto(review);
                }).collect(Collectors.toList());
        placeDetailDto.setReviews(reviewDtoList);
        return placeDetailDto;

    }


}

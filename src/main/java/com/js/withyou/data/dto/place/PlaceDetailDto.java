package com.js.withyou.data.dto.place;

import com.js.withyou.data.PlaceDepartment;
import com.js.withyou.data.dto.PlaceDepartmentDto;
import com.js.withyou.data.dto.review.ReviewDto;
import com.js.withyou.data.entity.Place.Place;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PlaceDetailDto {

    @NotBlank
    private Long placeId; //필수로 전달할 요소
    @NotBlank
    private String placeName; //필수로 전달할 요소

    private String placeRoadAddress;

    private double latitude;//위도 Y축

    private double longitude;//경도 X축

    private String categoryName; // Category 엔티티의 이름만 전달

    private String dongName; // dong엔티티의 이름만 전달

//    private String sigunguName; // Sigungu 엔티티의 이름만 전달


    private List<ReviewDto> reviews;


    private List<PlaceDepartmentDto> placeDepartments;


    public PlaceDetailDto convertToPlaceDetailDto(Place place) {

        List<ReviewDto> reviewDtoList = place.getReviews().stream()
                .map(review -> {
                    ReviewDto reviewDto = new ReviewDto();
                    return reviewDto.convertToReviewDto(review);
                }).collect(Collectors.toList());


        List<PlaceDepartmentDto> placeDepartmentDtoList = place.getPlaceDepartments().stream()
                .map(PlaceDepartment->{
                    PlaceDepartmentDto placeDepartmentDto = new PlaceDepartmentDto();
                   return placeDepartmentDto.convertToPlaceDepartmentDto(PlaceDepartment);
                }).collect(Collectors.toList());


        PlaceDetailDto placeDetailDto = PlaceDetailDto.builder()
                .placeId(place.getPlaceId())
                .placeName(place.getPlaceName())
                .placeRoadAddress(place.getPlaceRoadAddress())
                .categoryName(place.getCategory().getCategoryName())
                .dongName(place.getDong().getDongName())
                .longitude(place.getLocation().getX())
                .latitude(place.getLocation().getY())
                .reviews(reviewDtoList)
                .placeDepartments(placeDepartmentDtoList)
                .build();

        return  placeDetailDto;

//        placeDetailDto.setPlaceId(place.getPlaceId());
//        placeDetailDto.setPlaceName(place.getPlaceName());
//        placeDetailDto.setPlaceRoadAddress(place.getPlaceRoadAddress());
////        placeDetailDto.setPlaceLatitude(place.getPlaceLatitude());
////        placeDetailDto.setPlaceLongitude(place.getPlaceLongitude());
//        placeDetailDto.setCategoryName(place.getCategory().getCategoryName());
//        placeDetailDto.setDongName(place.getDong().getDongName());
//        List<ReviewDto> reviewDtoList = place.getReviews().stream()
//                .map(review -> {
//                    ReviewDto reviewDto = new ReviewDto();
//                    return reviewDto.convertToReviewDto(review);
//                }).collect(Collectors.toList());
//        placeDetailDto.setReviews(reviewDtoList);
//        return placeDetailDto;
//
    }


}

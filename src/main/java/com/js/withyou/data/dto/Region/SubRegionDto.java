package com.js.withyou.data.dto.Region;

import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.SubRegion;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class SubRegionDto {
    @NotBlank(message = "세부지역 Id 정보 없음")
    private Long subRegionId;
    @NotBlank(message = "세부지역 이름 정보 없음")
    private String subRegionName; //
    @NotBlank(message = "지역 이름 정보 없음")
    private String regionName;
    private List<PlaceDto> places;

    public SubRegionDto convertToSubRegionDto(SubRegion subregion) {
        SubRegionDto subRegionDto = new SubRegionDto();
        subRegionDto.setSubRegionId(subregion.getSubRegionId());
        subRegionDto.setSubRegionName(subregion.getSubRegionName());
        //Place entity를 DTO로 변환하여 List에 저장
        List<PlaceDto> placeDtos = subregion.getPlaces().stream().map(place -> {
            PlaceDto placeDto = new PlaceDto();
            return placeDto.convertToPlaceDto(place);
        }).collect(Collectors.toList());
        subRegionDto.setPlaces(placeDtos);
        subRegionDto.setRegionName(subRegionDto.getRegionName());
        return subRegionDto;
    }
}

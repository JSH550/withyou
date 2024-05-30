package com.js.withyou.data.dto.Sigungu;

import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Region.Sigungu;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SigunguDto {
    @NotBlank(message = "세부지역 Id 정보 없음")
    private Long sigunguId;
    @NotBlank(message = "세부지역 이름 정보 없음")
    private String sigunguName; //
    @NotBlank(message = "지역 이름 정보 없음")
    private String sidoName;
    private List<PlaceDto> places;

    public SigunguDto convertToSigunguDto(Sigungu sigungu) {
        SigunguDto sigunguDto = new SigunguDto();
        sigunguDto.setSigunguId(sigungu.getSigunguId());
        sigunguDto.setSigunguName(sigungu.getSigunguName());
        //Place entity를 DTO로 변환하여 List에 저장
//        List<PlaceDto> placeDtos = sigungu.getPlaces().stream().map(place -> {
//            PlaceDto placeDto = new PlaceDto();
//            return placeDto.convertToPlaceDto(place);
//        }).collect(Collectors.toList());
//        sigunguDto.setPlaces(placeDtos);
        sigunguDto.setSidoName(sigunguDto.getSidoName());
        return sigunguDto;
    }
}

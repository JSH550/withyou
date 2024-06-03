package com.js.withyou.data.dto.place;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter

public class PlaceSearchRequestDto {

    private Long regionId;

    private String regionName;
    //    지역 데이터 타입입니다. sido sigungu dong
    private String dataType;

    //병원 카테고리입니다.
    private Long categoryId;

    @Digits(integer = 4, fraction = 0, message = "숫자 형식이어야 합니다.")
    //페이지 번호입니다.
    private Integer pageNumber;

    //
    private Integer departmentId;

    private String searchWord;//검색어입니다.

}

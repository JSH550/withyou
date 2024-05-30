package com.js.withyou.data.dto;

import com.js.withyou.data.PlaceDepartment;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString


public class PlaceDepartmentDto {


    //과 이름
    private String departmentName;
    //의사 수
    private Integer doctorCount;

    public PlaceDepartmentDto() {

    }

    public PlaceDepartmentDto convertToPlaceDepartmentDto(PlaceDepartment placeDepartment){
        PlaceDepartmentDto placeDepartmentDto = PlaceDepartmentDto.builder()
                .departmentName(placeDepartment.getDepartment().getDepartmentName())
                .doctorCount(placeDepartment.getDoctorCount())
                .build();

        return placeDepartmentDto;

    }



}

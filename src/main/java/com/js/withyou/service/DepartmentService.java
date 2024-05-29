package com.js.withyou.service;

import com.js.withyou.data.dto.DepartmentNameDto;

import java.util.List;

public interface DepartmentService {

    /**
     * 모든 진료과(department)를 반환하는 메서드입니다.
     * 치과, 한의학은 하나의 DTO로 반환합니다.
     * @return
     */
    List<DepartmentNameDto> getAllDepartmentNameDto();

}

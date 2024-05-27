package com.js.withyou.controller;

import com.js.withyou.data.dto.CategoryNameDto;
import com.js.withyou.data.dto.DepartmentNameDto;
import com.js.withyou.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/departments/list")

    public ResponseEntity<List<DepartmentNameDto>> returnDepartmentList(){
        List<DepartmentNameDto> departmentNameDtoList = departmentService.getAllDepartmentNameDto();
        return ResponseEntity.ok(departmentNameDtoList);
    }


}

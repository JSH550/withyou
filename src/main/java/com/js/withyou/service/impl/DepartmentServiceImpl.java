package com.js.withyou.service.impl;

import com.js.withyou.data.dto.DepartmentNameDto;
import com.js.withyou.data.entity.Department;
import com.js.withyou.repository.DepartmentRepository;
import com.js.withyou.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentNameDto> getAllDepartmentNameDto() {
        List<Department> departmentList = departmentRepository.findAll();
        List<DepartmentNameDto> departmentNameDtoList = departmentList.stream().map(Department -> DepartmentNameDto.builder()
                .departmentId(Department.getDepartmentId())
                .departmentName(Department.getDepartmentName())
                .build()).collect(Collectors.toList());

        return departmentNameDtoList;
    }
}

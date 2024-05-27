package com.js.withyou.repository;


import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository  extends JpaRepository<Department,Integer> {

    Optional<Department> findByDepartmentName(String name);

    List<Department> findByDepartmentNameContaining(String keyword);
}

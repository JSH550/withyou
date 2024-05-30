package com.js.withyou.data.entity;

import com.js.withyou.data.PlaceDepartment;
import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;

    @Column(nullable = false,length = 30)

    private String departmentName;

    @Column(nullable = false,length = 10)
    private String departmentType;

    @OneToMany(mappedBy = "department",cascade =CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<PlaceDepartment> placeDepartments = new ArrayList<>();



}

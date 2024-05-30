package com.js.withyou.data;

import com.js.withyou.data.entity.Department;
import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
public class PlaceDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long placeDepartmentId;


    @ManyToOne
    @JoinColumn( name = "place_id",nullable = false)
    private Place place;



    @ManyToOne
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;


    private Integer doctorCount;






}

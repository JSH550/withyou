package com.js.withyou.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Region  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId ;

    @Column(nullable = false)//지역이름은 중복되면 안됨
    private String regionName; //

    //외래키는 subRegion이 가져감
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubRegion> subRegions = new ArrayList<>();




}

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

    @Column(nullable = false)//시도명은 null일수없음
    private String regionName; //

    // 줄임말 필드 추가
    @Column(nullable = false)
    private String regionShortName; // 시도명의 줄임말 필드,ex) 충남 충북

    //외래키는 subRegion이 가져감
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubRegion> subRegions = new ArrayList<>();




}

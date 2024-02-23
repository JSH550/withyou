package com.js.withyou.data.entity;

import com.js.withyou.data.dto.Region.SubRegionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
//@Table(name = "sub_regions")
public class SubRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subRegionId ;

    @Column//광주시같이 중복될 가능성 있음, 중복허용
    private String subRegionName; //

    @ManyToOne
    @JoinColumn(name = "region_id",nullable = false)//상위카테고리는 null이면 안된다.
    private Region region;

    @OneToMany(mappedBy = "subRegion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Place> places = new ArrayList<>();
    public static SubRegion createSubRegion(String subRegionName, Region region) {
        SubRegion subRegion = new SubRegion();
        subRegion.subRegionName = subRegionName;
        subRegion.region = region;
        return subRegion;
    }





}

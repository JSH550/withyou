package com.js.withyou.data.entity;

import com.js.withyou.data.dto.Region.SubRegionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
//@Table(name = "sub_regions")
public class SubRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subRegionId ;

    @Column(unique = true)//지역이름은 중복되면 안됨
    private String subRegionName; //

    @ManyToOne
    @JoinColumn(name = "region_id",nullable = false)//상위카테고리는 null이면 안된다.
    private Region region;

    public static SubRegion createSubRegion(String subRegionName, Region region) {
        SubRegion subRegion = new SubRegion();
        subRegion.subRegionName = subRegionName;
        subRegion.region = region;
        return subRegion;
    }





}

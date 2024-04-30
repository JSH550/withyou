package com.js.withyou.data.entity.Region;


import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class DongRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dongRegionId ;

    @Column//중복된 동이름 존재
    private String dongRegionName; //

    @ManyToOne
    @JoinColumn(name = "subRegion_id",nullable = false)//상위카테고리는 null이면 안된다.
    private SubRegion subRegion;

    @OneToMany(mappedBy = "dongRegion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Place> places = new ArrayList<>();
}

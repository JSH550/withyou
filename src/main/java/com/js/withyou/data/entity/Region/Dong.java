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
public class Dong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dongId ;

    @Column//중복된 동이름 존재
    private String dongName; //

    @ManyToOne
    @JoinColumn(name = "sigungu_id",nullable = false)//상위카테고리는 null이면 안된다.
    private Sigungu sigungu;

    @OneToMany(mappedBy = "dong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Place> places = new ArrayList<>();
}

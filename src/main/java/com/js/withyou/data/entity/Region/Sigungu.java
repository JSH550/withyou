package com.js.withyou.data.entity.Region;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
//@Table(name = "sub_regions")
public class Sigungu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sigunguId ;

    @Column//광주시같이 중복될 가능성 있음, 중복허용
    private String sigunguName; //

    @ManyToOne
    @JoinColumn(name = "sido_id",nullable = false)//상위카테고리는 null이면 안된다.
    private Sido sido;

    @OneToMany(mappedBy = "sigungu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dong> dongs = new ArrayList<>();


//    @OneToMany(mappedBy = "sigungu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Place> places = new ArrayList<>();

    public static Sigungu createSigungu(String sigunguName, Sido sido) {
        Sigungu sigungu = new Sigungu();
        sigungu.sigunguName = sigunguName;
        sigungu.sido = sido;
        return sigungu;
    }





}

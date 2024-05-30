package com.js.withyou.data.entity.Region;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@RequiredArgsConstructor
public class Sido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sidoId;

    @Column(nullable = false)//시도명은 null일수없음
    private String sidoName; //

    // 줄임말 필드 추가
    @Column(nullable = false)
    private String sidoShortName; // 시도명의 줄임말 필드,ex) 충남 충북

    //외래키는 sigungu이 가져감
    @OneToMany(mappedBy = "sido", cascade = CascadeType.ALL,fetch = LAZY)
    private List<Sigungu> sigungus = new ArrayList<>();




}

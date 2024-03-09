package com.js.withyou.data.entity.Place;

import com.js.withyou.data.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    //  리뷰의 내용
    private String reviewContent;

    //  별점
    private Integer reviewRating;

    //  리뷰 작성 시간
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime reviewTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;


}

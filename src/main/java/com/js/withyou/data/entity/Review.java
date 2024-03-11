package com.js.withyou.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String reviewContent;

    //  별점
    private Integer reviewRating;

    //  리뷰 작성 시간을 자동으로 생성합니다.
    @CreationTimestamp
    private LocalDateTime reviewTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties("reviews")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @JsonIgnoreProperties("reviews")
    private Place place;

    public Review() {

    }


    public Review saveReview (ReviewCreateDto reviewCreateDto,Member member, Place place) {
        Review review = Review.builder()
                .reviewContent(reviewCreateDto.getReviewContent())
                .reviewRating(reviewCreateDto.getReviewRating())
                .member(member)
                .place(place)
                .build();

        return review;
    }
}

package com.js.withyou.service.impl;

import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.Review;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceImplTest {
    private final ReviewService reviewService;
    private final PlaceService placeService;
    private final MemberService memberService;

    @Autowired
    ReviewServiceImplTest(ReviewService reviewService, PlaceService placeService, MemberService memberService) {
        this.reviewService = reviewService;
        this.placeService = placeService;
        this.memberService = memberService;
    }


    @Test
    public void createReviewTest() {



        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setReviewContent("테스트문구입니다.");
        reviewCreateDto.setReviewRating(1);
        reviewCreateDto.setPlaceId(2260L);
        reviewCreateDto.setUserEmail("kim@gmail.com");
//        Review review = reviewService.createReview(reviewCreateDto);
//
//        System.out.println(review.toString());

    }
}
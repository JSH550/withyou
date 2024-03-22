package com.js.withyou.service;

import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.dto.ReviewDto;
import com.js.withyou.data.dto.place.ReviewMypageDto;
import com.js.withyou.data.entity.Review;
import com.js.withyou.repository.ReviewRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewService {

    public List<Review> getReviewsByPlace(Long PlaceId);

    void createReview(ReviewCreateDto reviewCreateDto);

    List<ReviewMypageDto> getReviewsByMemberEmail(@Param("memberEmail") String memberEmail);
}

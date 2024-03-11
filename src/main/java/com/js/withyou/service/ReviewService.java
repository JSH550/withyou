package com.js.withyou.service;

import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.entity.Review;
import com.js.withyou.repository.ReviewRepository;

import java.util.List;

public interface ReviewService {

    public List<Review> getReviewsByPlace(Long PlaceId);

    void createReview(ReviewCreateDto reviewCreateDto);
}

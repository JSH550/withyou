package com.js.withyou.data.dto.review;

import com.js.withyou.data.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReviewDto {

    private Long reviewId;



    private String reviewContent;

    private Integer reviewRating;

    private LocalDateTime reviewCreateDate;

    private LocalDateTime reviewLastModifiedDate;


    private String memberName;

    private String memberEmail;

    private String placeName;


    public ReviewDto convertToReviewDto(Review review) {

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewContent(review.getReviewContent())
                .reviewRating(review.getReviewRating())
                .reviewCreateDate(review.getReviewCreateDate())
                .reviewLastModifiedDate(review.getReviewLastModifiedDate())
                .memberEmail(review.getMember().getMemberEmail())
                .memberName(review.getMember().getMemberName())
                .placeName(review.getPlace().getPlaceName())
                .build();
        return reviewDto;

    }
}

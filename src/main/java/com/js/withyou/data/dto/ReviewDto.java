package com.js.withyou.data.dto;

import com.js.withyou.data.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {

    private Long reviewId;

    private String reviewContent;

    private Integer reviewRating;

    private LocalDateTime reviewTime;

    private String memberName;


    public ReviewDto convertToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setReviewId(review.getReviewId());
        reviewDto.setReviewContent(review.getReviewContent());
        reviewDto.setReviewRating(review.getReviewRating());
        reviewDto.setReviewTime(review.getReviewTime());
        reviewDto.setMemberName(review.getMember().getMemberName());
//        MemberNameDto memberNameDto = new MemberNameDto();
//        reviewDto.setMemberName(memberNameDto.convertToMemberNameDto(review.getMember()));

        return reviewDto;

    }
}

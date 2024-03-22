package com.js.withyou.data.dto.place;

import com.js.withyou.data.dto.ReviewDto;
import com.js.withyou.data.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
//유저가 자기가 작성한 댓글을 확인할때 사용하는 DTO 입니다.
public class ReviewMypageDto {


    private Long reviewId;

    private String reviewContent;

    private Integer reviewRating;

    private LocalDateTime reviewTime;

    private String placeName;

    public ReviewMypageDto convertToReviewMypageDto(Review review) {
        ReviewMypageDto reviewMypageDto = new ReviewMypageDto();

        reviewMypageDto.setReviewId(review.getReviewId());
        reviewMypageDto.setReviewContent(review.getReviewContent());
        reviewMypageDto.setReviewRating(review.getReviewRating());
        reviewMypageDto.setReviewTime(review.getReviewTime());
        reviewMypageDto.setPlaceName(review.getPlace().getPlaceName());
//        MemberNameDto memberNameDto = new MemberNameDto();
//        reviewDto.setMemberName(memberNameDto.convertToMemberNameDto(review.getMember()));

        return reviewMypageDto;

    }



}

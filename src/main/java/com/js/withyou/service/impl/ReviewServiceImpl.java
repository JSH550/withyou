package com.js.withyou.service.impl;

import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.dto.ReviewDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.place.ReviewMypageDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.Review;
import com.js.withyou.repository.ReviewRepository;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final PlaceService placeService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, MemberService memberService, PlaceService placeService) {
        this.reviewRepository = reviewRepository;
        this.memberService = memberService;
        this.placeService = placeService;
    }

    /**
     * 특정 장소(place)에 작성된 리뷰들을 가져옵니다.
     */


    @Override
    public List<Review> getReviewsByPlace(Long PlaceId) {
//        reviewRepository.
        return null;
    }


    /**
     * 유저가 작성한 리뷰를
     * //     * @param  reviewCreateDto reviewCreateDto 유저가 작성한 리뷰 정보를 담은 DTO
     */
    public void createReview(ReviewCreateDto reviewCreateDto) {
        Review review = new Review();

        Place foundPlace = placeService.getPlaceEntityByPlaceId(reviewCreateDto.getPlaceId());

        review.setPlace(foundPlace);

        Member foundMember = memberService.getMemberEntityByEmail(reviewCreateDto.getUserEmail());
        review.setMember(foundMember);


        Review savedReview = reviewRepository.save(review.saveReview(reviewCreateDto, foundMember, foundPlace));
//        return savedReview;

    }

    ;


    /**
     *
     * @param memberEmail
     * @return
     */
    @Override
    public List<ReviewMypageDto> getReviewsByMemberEmail(String memberEmail) {
        List<Review> reviewByMemberEmail = reviewRepository.findReviewByMemberEmail(memberEmail);

        List<ReviewMypageDto> reviewMypageDtos = reviewByMemberEmail.stream().map(review -> {
            ReviewMypageDto reviewMypageDto = new ReviewMypageDto();
            return reviewMypageDto.convertToReviewMypageDto(review);
        }).collect(Collectors.toList());

        for (ReviewMypageDto reviewMypageDto : reviewMypageDtos) {
        }


        return reviewMypageDtos;
    }


    ;
}

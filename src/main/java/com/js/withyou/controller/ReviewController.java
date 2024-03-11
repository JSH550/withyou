package com.js.withyou.controller;

import com.js.withyou.data.dto.ReviewCreateDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final PlaceService placeService;

    public ReviewController(ReviewService reviewService, PlaceService placeService) {

        this.reviewService = reviewService;
        this.placeService = placeService;
    }

    @GetMapping("/review")
    public String showReviewForm(@RequestParam(required = false) Long placeId, Model model){

        placeId= 2270L;
        PlaceDto placeByPlaceId = placeService.getPlaceByPlaceId(placeId);
        model.addAttribute("placeDto",placeByPlaceId);

        return "/place/place-review-form";
    }

    @ResponseBody
    @PostMapping("/review")
    public String saveReview(@RequestParam(name = "content") String content){

        ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
        reviewCreateDto.setReviewContent(content);
        reviewCreateDto.setReviewRating(3);
        reviewCreateDto.setUserEmail("kim@gmail.com");
        reviewCreateDto.setPlaceId(2253L);

        reviewService.createReview(reviewCreateDto);

        return "ok";
    }


}

package com.js.withyou.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReviewCreateDto {

    private String reviewContent;
    //  별점
    private Integer reviewRating;
    //  리뷰 작성 시간
    private String userEmail;//Email 정보만 서비스로 전달하여, 서비스계층에서 사용자를 조회합니다.
    private Long placeId;
}

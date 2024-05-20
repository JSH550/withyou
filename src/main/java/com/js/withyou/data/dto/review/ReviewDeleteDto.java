package com.js.withyou.data.dto.review;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeleteDto {

    private Long reviewId;

    private String memberEmail;


}

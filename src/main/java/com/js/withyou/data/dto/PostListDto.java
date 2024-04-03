package com.js.withyou.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private Long postId;

    private String postTitle;

    private String memberName;
}

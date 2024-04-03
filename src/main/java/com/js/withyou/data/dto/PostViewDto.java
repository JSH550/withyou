package com.js.withyou.data.dto;

import com.js.withyou.data.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostViewDto {

    private Long postId;

    private String postTitle;

    private String postContent;

    private String memberName;

    //포맷하여 전달하므로 stirng 타입으로 전달됩니다.
    private String postCreateDate;

    private LocalDateTime postLastModifiedDate;



}

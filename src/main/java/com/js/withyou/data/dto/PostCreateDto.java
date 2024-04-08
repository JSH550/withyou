package com.js.withyou.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
public class PostCreateDto {

    @NotBlank
    @Size(max = 50)
    private String postTitle;

    @NotBlank
    @Size(max = 2000)
    private String postContent;
}

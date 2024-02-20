package com.js.withyou.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class CategoryDto {
    @NotBlank
    private Long categoryId;
    @NotBlank
    private String categoryName;
}

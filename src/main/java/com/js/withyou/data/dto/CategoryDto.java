package com.js.withyou.data.dto;

import com.js.withyou.data.dto.place.PlaceDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryDto {
    @NotBlank
    private Long categoryId;
    @NotBlank
    private String categoryName;

    private List<PlaceDto> places;

}

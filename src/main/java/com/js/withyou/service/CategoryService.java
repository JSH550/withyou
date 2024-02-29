package com.js.withyou.service;

import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.Region.SubRegionCreateDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceDto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface CategoryService {
    CategoryDto  updateSubRegion(SubRegionDto subRegionDto);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long categoryId);

//    List<CategoryDto> findCategoryByKeyword(String keyword);


}

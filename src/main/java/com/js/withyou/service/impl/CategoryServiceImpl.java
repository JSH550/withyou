package com.js.withyou.service.impl;

import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.repository.CategoryRepository;
import com.js.withyou.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        //categoryId로 조회
        Optional<Category> foundCategory = categoryRepository.findById(categoryId);
        return convertToCategoryDto(foundCategory.get());
    }

    //키워드로 category Entity 검색하는 메서드


    @Override
    public CategoryDto updateSubRegion(SubRegionDto subRegionDto) {
        return null;
    }


    //모든 category 꺼내기 기능
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        ArrayList<CategoryDto> categoryDtoList= new ArrayList<>();
        for (Category category : categories) {
            categoryDtoList.add(convertToCategoryDto(category));
        }
        return categoryDtoList;
    }

//    Category entity 를 DTO로 변환
    public CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setCategoryName(category.getCategoryName());
        return categoryDto;
    }

}

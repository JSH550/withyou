package com.js.withyou.controller;

import com.js.withyou.data.dto.CategoryNameDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /**
     * 카테고리 list를 전달하는 API입니다.
     * @return
     */
    @GetMapping("/categories/list")
    @ResponseBody
    public ResponseEntity<List<CategoryNameDto>> returnCategoryList(){

        List<CategoryNameDto> categoryNameDtoList = categoryService.getAllCategoriesNameDto();

        return ResponseEntity.ok(categoryNameDtoList);


    }




}

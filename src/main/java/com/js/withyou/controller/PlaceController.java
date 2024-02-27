package com.js.withyou.controller;

import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.service.CategoryService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.RegionService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class PlaceController {

    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final RegionService regionService;

    public PlaceController(CategoryService categoryService, PlaceService placeService, RegionService regionService) {
        this.categoryService = categoryService;
        this.placeService = placeService;
        this.regionService = regionService;
    }

    @GetMapping(value = "/places/new")
    public String showPlaceAddForm(Model model) {
        //모든 category list 전송하여 drop down으로 구현
        List<CategoryDto> allCategories = categoryService.getAllCategories();
        List<RegionDto> allRegions = regionService.getAllRegions();


        model.addAttribute("categories", allCategories);
        model.addAttribute("regions", allRegions);

        return "/place/place-addform";

    }

    //place의 Detail을 보여주는 메서드
//    @ResponseBody
    @GetMapping("/places/{placeId}")
    public String showPlaceDetail(@PathVariable @NotNull Long placeId,
                                  Model model) {
        log.info("요청된 placeId={}",placeId);
//        id값 조회해서 해당 place 정보 view로 넘겨주세요
        PlaceDto placeDto = placeService.findPlaceByPlaceId(placeId);
        model.addAttribute("placeDto",placeDto);
        return "/place/place-detail";
    }


    @GetMapping("/map")
    public String showMapPage() {
        return "/map/map";
    }

}

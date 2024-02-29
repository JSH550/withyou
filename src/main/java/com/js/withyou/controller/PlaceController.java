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
import org.springframework.web.bind.annotation.*;

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
        log.info("요청된 placeId={}", placeId);
//        id값 조회해서 해당 place 정보 view로 넘겨주세요
        PlaceDto placeDto = placeService.findPlaceByPlaceId(placeId);
        log.info("요청된 장소의 위도={},경도={}", placeDto.getPlaceLatitude(), placeDto.getPlaceLongitude());

        model.addAttribute("placeDto", placeDto);
        return "/place/place-detail";
    }

    @GetMapping("/search")
    public String showSearchPlace(@RequestParam(name = "query", required = false) String searchKeyword
            , Model model) {

        log.info("searchKeyword={}", searchKeyword);
        //쿼리파라미터 값이 null 이거나, 쿼리파라미터가 null이면 model값없이 view로 이동
        if (searchKeyword == null||searchKeyword.isEmpty()) {
            log.info("검색어 미입력");
            return "/place/place-search";
        }

//        //시설 이름 으로 검색
//        List<PlaceDto> placeDtoList = placeService.findPlaceByKeyword(searchKeyword);
//        //검색한 결과가 없을경우 search page로 리다이렉트
//        model.addAttribute("placeDtoList", placeDtoList);


        //시설 카테고리로 검색
        List<PlaceDto> placeDtoList = placeService.findPlaceByCategory(searchKeyword);

        //상세 지역으로 검색
        //1.시도 정보로 검색 -> 풀네임, 줄임말 둘다임
//        placeService.
        //2. 시군구 정보로 검색


        model.addAttribute("placeDtoList", placeDtoList);
        return "/place/place-search";
    }

    ;

//    @GetMapping("/search")
//    public String searchPlaces(@RequestParam(required = false) String searchWord,
//                               Model model){
//        if (searchWord.isEmpty()){
//            return "/place/place-search";
//        }
//
//        //시설 이름 으로 검색
//        List<PlaceDto> placeDtoList = placeService.findPlaceByKeyword(searchWord);
//        //검색한 결과가 없을경우 search page로 리다이렉트
//        model.addAttribute("placeDtoList",placeDtoList);
//
//
//        //시설 카테고리로 검색
//
//        //상세 지역으로 검색
//        return "/place/place-search";
//
//    }


    @GetMapping("/map")
    public String showMapPage() {
        return "/map/map";
    }

}

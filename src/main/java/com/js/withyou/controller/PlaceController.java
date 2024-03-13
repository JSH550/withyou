package com.js.withyou.controller;

import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
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

    /**
     * 시설(place) 의 상세정보를 보여주는 메서드입니다.
     * @param placeId
     * @param model
     * @return
     */

    //    @ResponseBody
    @GetMapping("/places/{placeId}")
    public String showPlaceDetail(@PathVariable @NotNull Long placeId,
                                  Model model) {
        log.info("요청된 placeId={}", placeId);
//        id값 조회해서 해당 place 정보 view로 넘겨주세요
        PlaceDetailDto foundPlaceDto = placeService.getPlaceDetailDtoByPlaceId(placeId);
//        log.info("요청된 장소의 위도={},경도={}", foundPlaceDto.getPlaceLatitude(), foundPlaceDto.getPlaceLongitude());
        log.info("요청된 장소의 정보", foundPlaceDto.toString());

        model.addAttribute("placeDto", foundPlaceDto);

        return "/place/place-detail";
    }

    public void addPlaceDtoList(List<PlaceDto> placeDtoList, List<PlaceDto> foundPlaceDtoList){
        if (!foundPlaceDtoList.isEmpty()) {
            for (PlaceDto placeDto : foundPlaceDtoList) {
                placeDtoList.add(placeDto);
            }
        }
    };

    /*
    * 클라이언트가 검색어를 입력하면 그 검색어를 바탕으로 시설을 검색합니다.
    * 1.키워드로 카테고리 검색 후 반환값이 null 이 아니면, 해당 카테고리 ID와 매핑된 시설을 저장합니다.
    * 2.키워드로 시설명 검색 후 반환값이 null 이 아니면, 해당 시설들을 저장합니다.
    * 3.키워드로 시도 정보 검색 후 반환값이 null 이 아니면, 해당 시도 하위 시군구 정보로 매핑된 시설을 저장합니다.
    * 4.저장된 시설 정보를 클라이언트에게 반환합니다.
     */
    @GetMapping("/search")
    public String showSearchPlace(@RequestParam(name = "query", required = false) String searchKeyword
            , Model model) {

        log.info("searchKeyword={}", searchKeyword);
        if (searchKeyword == null||searchKeyword.isEmpty()) {//쿼리파라미터 값이 null 이거나, 쿼리파라미터가 null이면  view로 이동
            log.info("검색어 미입력");
            return "/place/place-search";
        }


        List<PlaceDto> placeDtoList = placeService.searchPlacesByKeyWord(searchKeyword);
        model.addAttribute("placeDtoList",placeDtoList);
        return "/place/place-search";
        }

        //1.시도 정보로 검색 -> 풀네임, 줄임말 둘다임
//        placeService.
        //2. 시군구 정보로 검색

    /**
     *
     * @param model
     * @return
     */

    @GetMapping("/places/select")
    public String showRegionSelectPage(Model model){
        List<RegionDto> regionDtoList = regionService.getAllRegions();
        model.addAttribute("regionDtoList",regionDtoList);
        return "/place/place-region-select";
    }


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


//    @GetMapping("/map")
//    public String showMapPage() {
//        return "/map/map";
//    }
//
//}

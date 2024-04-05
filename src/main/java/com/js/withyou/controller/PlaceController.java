package com.js.withyou.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.js.withyou.customClass.CustomUser;
import com.js.withyou.data.KeywordDto;
import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.Region.RegionNameDto;
import com.js.withyou.data.dto.SubRegion.SubRegionSearchDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.SearchSuggestionDto;
import com.js.withyou.service.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class PlaceController {

    private final CategoryService categoryService;
    private final PlaceService placeService;
    private final RegionService regionService;

    private final SubRegionService subRegionService;
    private final MemberLikePlaceService memberLikePlaceService;

    public PlaceController(CategoryService categoryService, PlaceService placeService, RegionService regionService, SubRegionService subRegionService, MemberLikePlaceService memberLikePlaceService) {
        this.categoryService = categoryService;
        this.placeService = placeService;
        this.regionService = regionService;
        this.subRegionService = subRegionService;
        this.memberLikePlaceService = memberLikePlaceService;
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
     *
     * @param placeId
     * @param model
     * @return
     */

    //    @ResponseBody
    @GetMapping("/places/{placeId}")
    public String showPlaceDetail(@PathVariable @NotNull Long placeId,
                                  @SessionAttribute(value = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail,
                                  Model model) {
//        if (memberEmail.isEmpty()) {
//            memberEmail = "kim@gmail.com";
//        }
        log.info("요청된 placeId={}", placeId);
//        id값 조회해서 해당 place 정보 view로 넘겨주세요
        PlaceDetailDto foundPlaceDto = placeService.getPlaceDetailDtoByPlaceId(placeId);
//        log.info("요청된 장소의 위도={},경도={}", foundPlaceDto.getPlaceLatitude(), foundPlaceDto.getPlaceLongitude());
        log.info("요청된 장소의 정보", foundPlaceDto.toString());

        model.addAttribute("placeDto", foundPlaceDto);

        return "/place/place-detail";
    }

    public void addPlaceDtoList(List<PlaceDto> placeDtoList, List<PlaceDto> foundPlaceDtoList) {
        if (!foundPlaceDtoList.isEmpty()) {
            for (PlaceDto placeDto : foundPlaceDtoList) {
                placeDtoList.add(placeDto);
            }
        }
    }

    ;

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
        if (searchKeyword == null || searchKeyword.isEmpty()) {//쿼리파라미터 값이 null 이거나, 쿼리파라미터가 null이면  view로 이동
            log.info("검색어 미입력");
            return "/place/place-search";
        }


        List<PlaceDto> placeDtoList = placeService.searchPlacesByKeyWord(searchKeyword);
        model.addAttribute("placeDtoList", placeDtoList);
        return "/place/place-search";
    }


    @ResponseBody
    @PostMapping("/search")
    public ResponseEntity<List<PlaceDto>> returnSearchResult(@RequestBody SearchSuggestionDto searchSuggestionDto,
                                                             Model model) {

        log.info("search 요청 값={}",searchSuggestionDto.toString());
        if (searchSuggestionDto==null) {
            List<PlaceDto> placeDtoList = new ArrayList<>();

            return ResponseEntity.ok(placeDtoList);
        }


        //타입하고 id 받아오기
        //1. region으로 place 검색하기
        //2. subregion으로 place 검색하기.


        List<PlaceDto> placeDtoList = placeService.searchPlacesByKeyWord(searchSuggestionDto.getName());
        for (PlaceDto placeDto : placeDtoList) {
            log.info("검색된값 = {}",placeDto.toString());

        }
        return ResponseEntity.ok(placeDtoList);

    }


    /**
     * 유저가 검색창에 입력한 값으로 추천 검색 수행
     * 1. 시군구에서 정보 검색 있으면 return 없으면 읍면동으로 검색 진행
     * - 읍면동으로 검색 결과 있으면 return 없으면 return x
     * 2. 시설명에서 검색
     * - 검색 결과 있으면 return 없으면 return x 페이지네이션 필요
     */
    @ResponseBody
    @PostMapping("/search/suggest")
    public ResponseEntity<List<SearchSuggestionDto>> showSearchSuggest(@RequestBody KeywordDto keyword,
                                                                       Model model) {
        //1. 지역(region) 이름으로 검색
        List<SearchSuggestionDto> searchSuggestionDtoList = new ArrayList<>();//클라이언트에게 전달하기 위한 DTO List 입니다.

        log.info("유저가 요청한 키워드 ={}", keyword.getKeyword());
        List<RegionNameDto> regionNameDtoByKeyword = regionService.getRegionNameDtoByKeyword(keyword.getKeyword());
        if (!regionNameDtoByKeyword.isEmpty()) {
            for (RegionNameDto regionNameDto : regionNameDtoByKeyword) {
//                SearchSuggestionDto.builder().dataType("region").name(regionNameDto.getRegionName()).build();
                searchSuggestionDtoList.add(
                        SearchSuggestionDto.builder()
                                .id(regionNameDto.getRegionId())
                                .dataType("region")
                                .name(regionNameDto.getRegionName())
                                .build()
                );
            }
        }

        //table
        //name
        //id

        //2. 세부지역(subregion) 이름으로 검색
        List<SubRegionSearchDto> subRegionSearchDtosByKeyword = subRegionService.getSubRegionSearchDtosByKeyword(keyword.getKeyword());
        if (!subRegionSearchDtosByKeyword.isEmpty()) {
            for (SubRegionSearchDto subRegionSearchDto : subRegionSearchDtosByKeyword) {
                searchSuggestionDtoList.add(SearchSuggestionDto.builder()
                        .id(subRegionSearchDto.getSubRegionId())
                        .dataType("subRegion")
                        .name(subRegionSearchDto.getSubRegionNameLong())
                        .build());
            }
        }

        return ResponseEntity.ok(searchSuggestionDtoList);

//        return "ok";
    }


    //1.시도 정보로 검색 -> 풀네임, 줄임말 둘다임
//        placeService.
    //2. 시군구 정보로 검색

    /**
     * @param model
     * @return
     */

    @GetMapping("/places/select")
    public String showRegionSelectPage(Model model) {
        List<RegionDto> regionDtoList = regionService.getAllRegions();
        model.addAttribute("regionDtoList", regionDtoList);
        return "/place/place-region-select";
    }


    @ResponseBody
    @PostMapping("/places/{placeId}/is_favorite")
    public ResponseEntity<String> checkPlaceLike(@PathVariable(required = false) String placeId
            , Authentication authentication) {

//       시설(place) 정보 유무를 체크합니다.
        if (placeId == null || !placeId.matches("^[0-9]+$")) {
            log.info("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.");
            return new ResponseEntity<>("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.", HttpStatus.BAD_REQUEST);
        }
        //user 로그인 정보를 확인합니다.
        if (authentication == null) {
            log.info("로그인 정보 없음");
            return new ResponseEntity<>("false", HttpStatus.OK);//로그인 정보가 없으면 fasle (빈하트)
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        //테이블에서 좋아요를 눌렀는지 확인합니다. 있을겨우 true 없을경우 false
        boolean checkReuslt = memberLikePlaceService.checkMemberLikePlaceByPlaceAndMemberExistence(Long.valueOf(placeId), customUser.getUsername());
        if (checkReuslt) {
            return new ResponseEntity<>("true", HttpStatus.OK);//좋아요 눌렀으면 true (속이찬 하트)
        } else {
            return new ResponseEntity<>("false", HttpStatus.OK);//좋아요 정보가 없으면 false (속이빈 하트)
        }
    }


    /**
     * 유저가 시설(place)에 좋아요 버튼을 누르면 해당 정보를 저장하는 메서드입니다.
     *
     * @param placeId URL에 포함된 placeId를 문자열로 받습니다, 메서드에서 null체크와 숫자가 맞는지 체크합니다.
     * @return 요청 결과를 반환합니다. 잘못된 요청일경우 요청메시지를 보냅니다.
     */
    @ResponseBody
    @PostMapping("places/{placeId}/add_favorite")
    public ResponseEntity<String> addPlaceFavorite(@PathVariable(required = false) String placeId
            , @SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail,
                                                   Authentication authentication) {


        if (placeId == null || !placeId.matches("^[0-9]+$")) {
            log.info("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.");
            return new ResponseEntity<>("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.", HttpStatus.BAD_REQUEST);
        }
        //user 로그인 정보를 확인합니다.
        if (authentication == null) {
            log.info("로그인 정보 없음");
            return new ResponseEntity<>("false", HttpStatus.OK);
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        //좋아요 정보를 저장합니다. 성공했을경우 OK가 반환됩니다.
        String state = memberLikePlaceService.saveMemberLikePlace(Long.valueOf(placeId), customUser.getUsername());
        if (state == "OK") {
            return new ResponseEntity<>("요청성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("요청 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * 유저가 시설(place)에 좋아요 버튼을 눌르면 해당 정보를 삭제하는 메서드입니다.
     *
     * @param placeId URL에 포함된 placeId를 문자열로 받습니다, 메서드에서 null체크와 숫자가 맞는지 체크합니다.
     * @return 요청 결과를 반환합니다. 잘못된 요청일경우 요청메시지를 보냅니다.
     */
    @ResponseBody
    @PostMapping("places/{placeId}/delete_favorite")
    public ResponseEntity<String> removePlaceFavorite(@PathVariable(required = false) String placeId
            , @SessionAttribute(name = "LOGIN_MEMBER_EMAIL", required = false) String memberEmail
            , Authentication authentication
    ) {

        //URL의 placeId가 null이거나 숫자가 아닐경우 에러 메시지를 보냅니다.
        if (placeId == null || !placeId.matches("^[0-9]+$")) {
            log.info("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.");
            return new ResponseEntity<>("좋아요 요청 에러 : placeId가 Null이거나 정수가 숫자가아닙니다.", HttpStatus.BAD_REQUEST);
        }

        if (authentication == null) {
            log.info("로그인 정보 없음");
            return new ResponseEntity<>("false", HttpStatus.OK);
        }
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        //placeId로 조회된 값이 없으면 에러메시지를 보냅니다.

        String state = memberLikePlaceService.deleteMemberLikePlace(Long.valueOf(placeId), customUser.getUsername());
        if (state == "OK") {
            log.info("좋아요 삭제 요청 성공");
            return new ResponseEntity<>("요청성공", HttpStatus.OK);
        } else {
            log.info("좋아요 삭제 요청 실패");
            return new ResponseEntity<>("요청 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

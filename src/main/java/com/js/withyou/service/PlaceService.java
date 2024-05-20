package com.js.withyou.service;

import com.js.withyou.data.KeywordDto;
import com.js.withyou.data.dto.SearchSuggestionDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.data.dto.place.*;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.Region.Sigungu;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {

    void parsXmlFile();

//    void savePlace();

    void savePlace(PlaceCreateDto placeCreateDto, Category category, Sigungu sigungu);

    PlaceDto getPlaceByPlaceId(Long placeId);

    PlaceDetailDto getPlaceDetailDtoByPlaceId(Long placeId);


    Place getPlaceEntityByPlaceId(Long placeId);


    List<PlaceDto> getPlaceByKeyword(String keyword);

    /**
     * 특정 키워드를 포함한 시설(place) List를 페이지네이션으로 반환합니다.
     * @param keyword
     * @param pageable 페이지네이션을 위한 객체입니다.
     * @return PlaceListDto
     */
    List<PlaceListDto> getPlaceByKeywordAndPageable(String keyword, Pageable pageable);

    List<PlaceDto> getPlaceByCategory(String keyword);


    List<PlaceDto> getPlaceBySigunguIdList(List<Long> sigunguIdList);


    /**
     * 유저가 클릭한 추천 검색어를 바탕으로 시설(place) List를 반환합니다.
     * 추천검색어 타입에는 시도(sido), 시군구(sigungu)이 있으며 그외타입은 시설명(placeName)으로 검색합니다.
     * @param searchSuggestionDto 유저가 클릭한 추천검색어 입니다. 타입(시군구,시도 등)과 이름, priamry key, 페이지정보 를담고있습니다.
     * @return PlaceListDto DTO 형태로 List를 반환합니다.
     */
    List<PlaceListDto> getPlacesBySearchSuggestion(SearchSuggestionDto searchSuggestionDto);

    /**
     * 시군구(sigungu)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     *
     * @param sigunguId DB 검색을 위한 시군구()의 primary key 입니다.
     * @param pageable  페이지네이션을 위한 객체입니다.
     * @return PlaceListDto List 형식으로 반환합니다.
     */
    List<PlaceListDto> getPlaceBySigunguId(Long sigunguId, Pageable pageable);


    /**
     * 시도(sido)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     * @param sidoId DB 검색을 위한 시도(sido)의 primary key 입니다.
     * @param pageable 페이지네이션을 위한 객체입니다.
     * @return PlaceListDto
     */
    List<PlaceListDto> getPlaceListDtoBySidoId(Long sidoId, Pageable pageable);

    /**
     * 시군구(sigungu)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     * @param sigunguId
     * @param pageable
     * @return PlaceListDto
     */
    List<PlaceListDto> getPlaceListDtoBySigunguId(Long sigunguId, Pageable pageable);


    List<PlaceListDto>  getPlaceListDtoByPlaceSearchRequestDto(PlaceSearchRequestDto placeSearchRequestDTO , Pageable pageable);





    List<PlaceDto> getPlaceBySigungus(List<SigunguDto> sigunguDtoList);

    /**
     *
     * @param keyword
     * @return
     */
    List<PlaceDto> getPlaceDtoBySidoName(String keyword);

    /**
     * 컨트롤러에서 검색어를 넘겨받으면 그 검색어를 바탕으로 시설을 검색합니다.
     * 1.키워드로 카테고리 검색 후 반환값이 null 이 아니면, 해당 카테고리 ID와 매핑된 시설을 저장합니다.
     * 2.키워드로 시설명 검색 후 반환값이 null 이 아니면, 해당 시설들을 저장합니다.
     * 3.키워드로 시도 정보 검색 후 반환값이 null 이 아니면, 해당 시도 하위 시군구 정보로 매핑된 시설을 저장합니다.
     * @param searchKeyword 검색에 사용될 키워드 입니다.
     * @return List<PlaceDto> 저장된 시군구(Place)정보를 PlaceDto List로 반환합니다.
     */
    List<PlaceDto> searchPlacesByKeyWord(String searchKeyword);

    /**
     *
     * @param keywordDto
     * @return
     */
    List<SearchSuggestionDto> getSearchSuggestions(KeywordDto keywordDto);

    /**
     * 유저의 현재 위치 기반으로 시설(place)를 반환하는 메서드입니다.
     * @param latitude 유저의 위도입니다. 33.11~37.66
     * @param longitude 유저의 경도입니다. 124.60~131.87
     * @return
     */
    List<PlaceListDto> getPlaceListDtoListByUserLocation(double latitude, double longitude,Pageable pageable);








}

package com.js.withyou.service;

import com.js.withyou.data.KeywordDto;
import com.js.withyou.data.dto.SearchSuggestionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceCreateDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.place.PlaceListDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.Region.SubRegion;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {

    /**
     * 유저가 좋아요를 누른 시설(place)들을 DTO List로 반환하는 메서드 입니다.
     * @param memberEmail LikePlaceEntity 에서 유저(member)가 좋아요를 누른 place 정보들을 가져옵니다.
     * @return List<PlaceDto> PlaceDto형태로 반환합니다(검색창에서 검색했을때와 동일하게 view로 전송하기 위함입니다.)
     */
//    @Transactional
//    List<PlaceDto> getPlaceDtosByMemberEmail(String memberEmail);

    void parsXmlFile();

//    void savePlace();

    void savePlace(PlaceCreateDto placeCreateDto, Category category, SubRegion subRegion);

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


    //    List<PlaceDto> findPlaceBySubRegionId(List<Long> subRegionIdList);
    List<PlaceDto> getPlaceBySubRegionIdList(List<Long> subRegionIdList);


    /**
     * 유저가 클릭한 추천 검색어를 바탕으로 시설(place) List를 반환합니다.
     * 추천검색어 타입에는 시도(region), 시군구(subRegion)이 있으며 그외타입은 시설명(placeName)으로 검색합니다.
     * @param searchSuggestionDto 유저가 클릭한 추천검색어 입니다. 타입(시군구,시도 등)과 이름, priamry key, 페이지정보 를담고있습니다.
     * @return PlaceListDto DTO 형태로 List를 반환합니다.
     */
    List<PlaceListDto> getPlacesBySearchSuggestion(SearchSuggestionDto searchSuggestionDto);

    /**
     * 시군구(subRegion)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     * @param subRegionId DB 검색을 위한 시군구(subRegion)의 primary key 입니다.
     * @param pageable 페이지네이션을 위한 객체입니다.
     * @return PlaceListDto List 형식으로 반환합니다.
     */
    List<PlaceListDto> getPlaceBySubRegionId(Long subRegionId , Pageable pageable);


    /**
     * 시/도(region)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     * @param regionId DB 검색을 위한 시도(region)의 primary key 입니다.
     * @param pageable 페이지네이션을 위한 객체입니다.
     * @return PlaceListDto
     */
    List<PlaceListDto> getPlaceListDtoByRegionId(Long regionId, Pageable pageable);

    /**
     * 시군구(subRegion)에 속한 장소(place) List 를 페이지네이션으로 반환합니다.
     * @param subRegionId
     * @param pageable
     * @return PlaceListDto
     */
    List<PlaceListDto> getPlaceListDtoBySubregionId(Long subRegionId, Pageable pageable);





    List<PlaceDto> getPlaceBySubRegions(List<SubRegionDto> subRegionDtoList);

    List<PlaceDto> getPlaceByRegionName(String keyword);

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







//    List<PlaceListDto> getPlacesByRegionId(Long regionId);

}

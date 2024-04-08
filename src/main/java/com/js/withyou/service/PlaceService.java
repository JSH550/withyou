package com.js.withyou.service;

import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceCreateDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.place.PlaceListDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.SubRegion;

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

    List<PlaceDto> getPlaceByCategory(String keyword);


    //    List<PlaceDto> findPlaceBySubRegionId(List<Long> subRegionIdList);
    List<PlaceDto> getPlaceBySubRegionIdList(List<Long> subRegionIdList);


    /**
     * 특정 subRegion와 관계가 있는 place를 검색하여 반환합니다.
     * @param subRegionId
     * @return PlaceListDto List 형식으로 반환합니다.
     */
    List<PlaceListDto> getPlaceBySubRegionId(Long subRegionId);
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
     * @param regionId
     * @return
     */
    List<PlaceListDto> getPlacesByRegionId(Long regionId);

}

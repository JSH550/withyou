package com.js.withyou.service;

import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceSaveDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.SubRegion;

import java.util.List;

public interface PlaceService {
    void parsXmlFile();

//    void savePlace();

    void savePlace(PlaceSaveDto placeSaveDto, Category category, SubRegion subRegion);

    PlaceDto findPlaceByPlaceId(Long placeId);

    List<PlaceDto> findPlaceByKeyword(String keyword);

    List<PlaceDto> findPlaceByCategory(String keyword);


    //    List<PlaceDto> findPlaceBySubRegionId(List<Long> subRegionIdList);
    List<PlaceDto> findPlaceBySubRegionId(List<Long> subRegionIdList);


    List<PlaceDto> findPlaceBySubRegions(List<SubRegionDto> subRegionDtoList);

    List<PlaceDto> findPlaceByRegionName(String keyword);

    List<PlaceDto> searchPlacesByKeyWord(String searchKeyword);

}

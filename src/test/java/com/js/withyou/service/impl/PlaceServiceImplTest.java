package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Place;
import com.js.withyou.data.entity.SubRegion;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaceServiceImplTest {
    private final PlaceService placeService;
    private final RegionService regionService;

    @Autowired
    PlaceServiceImplTest(PlaceService placeService, RegionService regionService) {
        this.placeService = placeService;
        this.regionService = regionService;
    }

    //keyword로 시설 찾기 메서드 test
    @Test
    public void findPlaceByKeywordTest(){
        List<PlaceDto> foundPlaceDtoList = placeService.findPlaceByKeyword("인천");
        for (PlaceDto placeDto : foundPlaceDtoList) {
            System.out.println(placeDto.toString());
        }



    }

    @Test

    public void findPlaceByCategoryTest(){
        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("요양");
        //검색결과가 null일경우, 반환되는값은 없다.
//        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("라면");

        foundPlaceList.stream()
                .map(PlaceDto::getPlaceName)
                .forEach(System.out::println);

    }

    @Test
    public void findPlaceBySubRegionId(){
        List<RegionDto> regionByKeyword = regionService.getRegionByKeyword("남도");
        List<Long> subRegionId = new ArrayList<>();
        for (RegionDto regionDto : regionByKeyword) {
            System.out.println("시작점");
            List<SubRegion> subRegions = regionDto.getSubRegions();
            for (SubRegion subRegion : subRegions) {
                subRegionId.add(subRegion.getSubRegionId());
                System.out.println("subRegion id 출력 시작");
                System.out.println(subRegion.getSubRegionId());
            }
        }


//        for (PlaceDto placeDto : placeDtoList) {
//            System.out.println("결과");
////            System.out.println(placeDto.getPlaceName());
////            System.out.println(placeDto.toString());
//        }


    }


    @Test
    public void xmlTest(){
        placeService.parsXmlFile();


    }
}
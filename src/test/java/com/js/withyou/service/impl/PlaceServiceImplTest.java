package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PlaceServiceImplTest {
    private final PlaceService placeService;
    private final RegionService regionService;



    @Autowired
    PlaceServiceImplTest(PlaceService placeService, RegionService regionService) {
        this.placeService = placeService;
        this.regionService = regionService;
    }


    /*
     * 검색어를 포함한 장소를 조회하고 저장하는 메서드입니다.
     * 이 메서드는 검색된 장소를 조회하기 위해 1회의 쿼리를 실행하며, 카테고리 조회1회, 시군(place) 조회 1회 3회의 쿼리를 실행합니다.
     * 시군구(subRegion) 은 left join fetch 로 쿼리 place 조회시 같이 조회됩니다.
     * */
    @Test
    public void findPlaceByKeywordTest(){
        List<PlaceDto> foundPlaceDtoList = placeService.findPlaceByKeyword("병원");
        for (PlaceDto placeDto : foundPlaceDtoList) {
            System.out.println(placeDto.toString());
        }



    }
    /*
     * 검색어를 포함한 카테고리를 조회하고, 해당 카테고리에 속하는 장소를 저장하는 메서드입니다.
     * 이 메서드는 검색된 카테고리를 조회하기 위해 1회의 쿼리를 실행하며, 각 검색된 카테고리에 매핑된 장소를 조회하기 위해 추가적인 n회의 쿼리를 실행합니다.
     */


    @Test
    public void findPlaceByCategoryTest(){

        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("병원");//출력결과: "병원" 을 포함한 카테고리의 place들 출력
//        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("방문");
        //검색결과가 null일경우, 반환되는값은 없다.
//        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("라면");
        foundPlaceList.stream()
                .map(PlaceDto::toString)
                .forEach(System.out::println);

    }

    @Test
    public void findPlaceBySubRegionId(){
        List<RegionDto> regionByKeyword = regionService.getRegionByKeyword("남도");
        List<Long> subRegionId = new ArrayList<>();
        for (RegionDto regionDto : regionByKeyword) {
            System.out.println("시작점");
            List<SubRegionDto> subRegions = regionDto.getSubRegions();
            for (SubRegionDto subRegion : subRegions) {
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
    public void searchPlacesByKeyWordTest(){
        List<PlaceDto> placeDtoList1 = placeService.searchPlacesByKeyWord("인천");
//        List<PlaceDto> placeDtoList2 = placeService.searchPlacesByKeyWord("요양병원");
        for (PlaceDto placeDto : placeDtoList1) {
            System.out.println(placeDto.getPlaceName());
        }

//        for (PlaceDto placeDto : placeDtoList2) {
//            System.out.println(placeDto.getPlaceName());
//        }


    }
    @Test
    public void findPlaceBySubRegionIdTest(){
        List<Long> list = new ArrayList<>();
        list.add(0,14L);
        List<PlaceDto> placeBySubRegionId = placeService.findPlaceBySubRegionId(list);
        for (PlaceDto placeDto : placeBySubRegionId) {
            placeDto.toString();
        }

    }

    @Test
    public  void  findPlaceBySubRegionsTest(){
        List<SubRegionDto> subRegionDtoList = regionService.findSubregionByKeyword("수원");
        List<PlaceDto> placeDtoList = placeService.findPlaceBySubRegions(subRegionDtoList);
        for (PlaceDto placeDto : placeDtoList) {
            System.out.println(placeDto.toString());
        }

    }

    // 키워드로 시도(region)을 찾고, 하위 시군구(subRegion)에 매핑된 장소(Place)를 찾는 테스트 메서드입니다
    @Test
    public void findPlaceByRegionTest(){
        List<PlaceDto> placeDtoList = placeService.findPlaceByRegionName("경기");
        for (PlaceDto placeDto : placeDtoList) {
            System.out.println(placeDto.toString());

        }


    }


    @Test
    public void xmlTest(){
        placeService.parsXmlFile();


    }

}
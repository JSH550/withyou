package com.js.withyou.service.impl;

import com.js.withyou.data.dto.PlaceDepartmentDto;
import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.place.PlaceListDto;
import com.js.withyou.data.dto.place.PlaceSearchRequestDto;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.SidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PlaceServiceImplTest {

    private final PlaceService placeService;

    @Autowired

    public PlaceServiceImplTest(PlaceService placeService) {
        this.placeService = placeService;
    }








    /*
     * 검색어를 포함한 장소를 조회하고 저장하는 메서드입니다.
     * 이 메서드는 검색된 장소를 조회하기 위해 1회의 쿼리를 실행하며, 카테고리 조회1회, 시군(place) 조회 1회 3회의 쿼리를 실행합니다.
     * 시군구(sigungu) 은 left join fetch 로 쿼리 place 조회시 같이 조회됩니다.
     * */
    @Test
    public void findPlaceByKeywordTest(){
        List<PlaceDto> foundPlaceDtoList = placeService.getPlaceByKeyword("병원");
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

        List<PlaceDto> foundPlaceList = placeService.getPlaceByCategory("병원");//출력결과: "병원" 을 포함한 카테고리의 place들 출력
//        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("방문");
        //검색결과가 null일경우, 반환되는값은 없다.
//        List<PlaceDto> foundPlaceList = placeService.findPlaceByCategory("라면");
        foundPlaceList.stream()
                .map(PlaceDto::toString)
                .forEach(System.out::println);

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
        List<PlaceDto> placeBySubRegionId = placeService.getPlaceBySigunguIdList(list);
        for (PlaceDto placeDto : placeBySubRegionId) {
            placeDto.toString();
        }

    }



    @Test
    public void getPlaceBySubRegionIdTest(){
        PageRequest pageRequest = PageRequest.of(2000, 10);
        List<PlaceListDto> placeBySubRegionId = placeService.getPlaceBySigunguId(13L, pageRequest);
        for (PlaceListDto placeListDto : placeBySubRegionId) {
            System.out.println(placeListDto.toString());
        }


    }



    // 키워드로 시도(region)을 찾고, 하위 시군구(subRegion)에 매핑된 장소(Place)를 찾는 테스트 메서드입니다
    @Test
    public void findPlaceByRegionTest(){
        List<PlaceDto> placeDtoList = placeService.getPlaceDtoBySidoName("경기");
        for (PlaceDto placeDto : placeDtoList) {
            System.out.println(placeDto.toString());

        }


    }

    @Test
    public void getPlaceDetailDtoByPlaceIdTest(){
        PlaceDetailDto foundPlaceDto = placeService.getPlaceDetailDtoByPlaceId(1010L);
        System.out.println(foundPlaceDto.toString());


    }

    @Test
    public void getPlacesByRegionId(){
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<PlaceListDto> placesByRegionId = placeService.getPlaceListDtoBySidoId(1L, pageRequest);
        for (PlaceListDto placeListDto : placesByRegionId) {
            System.out.println(  placeListDto.toString());

        }

    }

    @Test
    public void getPlaceByKeywordAndPageableTest(){
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<PlaceListDto> result = placeService.getPlaceByKeywordAndPageable("인천", pageRequest);
        for (PlaceListDto placeListDto : result) {
            System.out.println(placeListDto.toString());
        }


    }
    @Test
    public void getPlaceListDtoBySidoIdTest(){
        PageRequest pageRequest = PageRequest.of(0, 20);

        List<PlaceListDto> placeListDtoByRegionId = placeService.getPlaceListDtoBySidoId(1L,pageRequest);
        for (PlaceListDto placeListDto : placeListDtoByRegionId) {
            System.out.println(placeListDto.toString());
        }


    }

    @Test
    public void getPlaceListDtoBySubregionIdTest(){
        PageRequest pageRequest = PageRequest.of(0, 20);
        List<PlaceListDto> placeListDtoList = placeService.getPlaceListDtoBySigunguId(5L,pageRequest);

        for (PlaceListDto placeListDto : placeListDtoList) {
            System.out.println(placeListDto.toString());
        }

    }

    @Test
    public void getPlaceListDtoByPlaceSearchRequestDtoTest(){
        PageRequest pageRequest = PageRequest.of(0, 20);

        PlaceSearchRequestDto placeSearchRequestDto = new PlaceSearchRequestDto();

        placeSearchRequestDto.setCategoryId(16L);
//        placeSearchRequestDto.setDataType("region");
//        placeSearchRequestDto.setRegionId(1L);

//        placeSearchRequestDto.setCategoryId(16L);
//        placeSearchRequestDto.setDataType("subRegion");
//        placeSearchRequestDto.setRegionId(250L);


        placeSearchRequestDto.setCategoryId(16L);

        List<PlaceListDto> placeListDtoByPlaceSearchRequestDto = placeService.getPlaceListDtoByPlaceSearchRequestDto(placeSearchRequestDto,pageRequest);

        for (PlaceListDto placeListDto : placeListDtoByPlaceSearchRequestDto) {
            System.out.println(placeListDto.toString());

        }

    }


    /**
     * 유저의 지역으로 주변의 시설을 검색하는 기능
     */
    @Test
    public void getPlaceListDtoListByUserLocationTest(){

        PageRequest pageRequest = PageRequest.of(0, 20);

        double longitude = 127.1791797 ;
        double latitude =36.7756271;
        List<PlaceListDto> placeListDtoListByUserLocation = placeService.getPlaceListDtoListByUserLocation(latitude,longitude,pageRequest);
        for (PlaceListDto placeListDto : placeListDtoListByUserLocation) {
            System.out.println(placeListDto.toString());
        }


    }


    @Test
    public void getPlaceListDtoBySearchConditionsTest(){
        PlaceSearchRequestDto placeSearchRequestDto = new PlaceSearchRequestDto();
//        placeSearchRequestDto.setCategoryId(31L);
//        placeSearchRequestDto.setSearchWord("고려");

        placeSearchRequestDto.setDepartmentId(49);
//        placeSearchRequestDto.setDepartmentId(1);

//        placeSearchRequestDto.setDataType("dong");
//        placeSearchRequestDto.setRegionId(2172L);

                placeSearchRequestDto.setDataType("sigungu");
        placeSearchRequestDto.setRegionId(123L);

//        placeSearchRequestDto.setDataType("sido");
//        placeSearchRequestDto.setRegionId(9L);


//f

        System.out.println("DTO 정보 ="+placeSearchRequestDto.toString());




        PageRequest pageRequest = PageRequest.of(0, 1000);

        List<PlaceListDto> result = placeService.getPlaceListDtoBySearchConditions(placeSearchRequestDto,pageRequest);

        System.out.println("결과 size  = "+result.size());


        for (PlaceListDto placeListDto : result) {

            List<PlaceDepartmentDto> placeDepartments = placeListDto.getPlaceDepartments();

            System.out.println("place name ="+ placeListDto.getPlaceName()+"category="+ placeListDto.getCategoryName());

            System.out.println("dongRegion = "+placeListDto.getDongName());
            System.out.println("진료과목");
            for (PlaceDepartmentDto placeDepartment : placeDepartments) {
                System.out.println(placeDepartment.getDepartmentName());
            }

        }

    }

    @Test
    public void xmlTest(){
        placeService.parsXmlFile();


    }



}
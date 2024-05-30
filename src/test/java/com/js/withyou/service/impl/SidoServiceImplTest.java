package com.js.withyou.service.impl;


import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sido.SidoNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.SidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SidoServiceImplTest {
    private final SidoService sidoService;
    @Autowired

    SidoServiceImplTest(SidoService sidoService) {
        this.sidoService = sidoService;
    }




    @Test
    public void getAllRegionTest() {
        List<SidoDto> allRegions = sidoService.getAllSidoList();
        for (SidoDto allRegion : allRegions) {
            System.out.println(allRegion.getSidoId());
            System.out.println(allRegion.getSidoName());
        }

    }

    @Test
    public void getRegionByRegionShortNameTest() {
        SidoDto sidoDto = sidoService.getSidoDtoBySidoShortName("경기");
        System.out.println(sidoDto.getSidoShortName() + sidoDto.getSidoId() + sidoDto.getSidoName());
    }

    @Test
    public void getRegionByKeywordTest() {
//        List<SidoDto> foundSidoDtoList = sidoService.getRegionByKeyword("충청");
//        List<SidoDto> foundSidoDtoList = sidoService.getRegionByKeyword("충");
        //null 입력시 모든값 출력
//        List<SidoDto> foundSidoDtoList = sidoService.getRegionByKeyword("");
        //shortName 검색 test 결과 ok
        List<SidoDto> foundSidoDtoList = sidoService.getSidoDtoListByKeyword("충청");


        for (SidoDto sidoDto : foundSidoDtoList) {
            System.out.println(sidoDto.toString());
//            sidoDto.getSigungus().stream()
//                    .map(Sigungu::toString)
//                    .forEach(System.out::println);
        }
    }

    @Test
    public void findPlaceByKeywordTest() {
        List<SigunguDto> sigunguDtos = sidoService.findSigunguByKeyword("수원");
        for (SigunguDto sigunguDto : sigunguDtos) {
            System.out.println(sigunguDto.toString());
        }
    }

    @Test
    public void findSubRegionsByRegionNameContainingKeywordTest(){
        List<SigunguDto> result = sidoService.getSigungusBySidoNameContainingKeyword("경기");;
        if (result.isEmpty()){
            System.out.println("비었음");
        }
        for (SigunguDto sigunguDto : result) {
            System.out.println(sigunguDto.toString());
        }


    }
    @Test
    public void getRegionNameDtoByKeywordTest(){
        List<SidoNameDto> result = sidoService.getSidoNameDtoListByKeyword("서울");
        for (SidoNameDto sidoNameDto : result) {
            System.out.println(sidoNameDto.toString());

        }

    }



}
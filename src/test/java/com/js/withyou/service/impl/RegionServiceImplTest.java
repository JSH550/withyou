package com.js.withyou.service.impl;


import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.entity.SubRegion;
import com.js.withyou.service.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RegionServiceImplTest {
    private final RegionService regionService;

    @Autowired
    public RegionServiceImplTest(RegionService regionService) {
        this.regionService = regionService;
    }

    @Test
    public void getAllRegionTest(){
        List<RegionDto> allRegions = regionService.getAllRegions();
        for (RegionDto allRegion : allRegions) {
            System.out.println(allRegion.getRegionId());
            System.out.println(allRegion.getRegionName());
        }

    }

    @Test
    public void getRegionByRegionShortNameTest(){
        RegionDto regionDto = regionService.getRegionByRegionShortName("경기");
        System.out.println(regionDto.getRegionShortName()+regionDto.getRegionId()+regionDto.getRegionName());
    }

    @Test
    public void getRegionByKeywordTest(){
//        List<RegionDto> foundRegionDtoList = regionService.getRegionByKeyword("충청");
//        List<RegionDto> foundRegionDtoList = regionService.getRegionByKeyword("충");
        //null 입력시 모든값 출력
//        List<RegionDto> foundRegionDtoList = regionService.getRegionByKeyword("");
        //shortName 검색 test 결과 ok
        List<RegionDto> foundRegionDtoList = regionService.getRegionByKeyword("충청");


        for (RegionDto regionDto : foundRegionDtoList) {
            System.out.println(regionDto.toString());
//            regionDto.getSubRegions().stream()
//                    .map(SubRegion::toString)
//                    .forEach(System.out::println);
        }
    }

}
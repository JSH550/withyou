package com.js.withyou.service;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegionService {
    List<RegionDto> getAllRegions();
    RegionDto getRegionById(Long RegionId);

    RegionDto getRegionByRegionShortName(String RegionShortName);

    List<RegionDto> getRegionByKeyword(String keyword);

    List<SubRegionDto> findSubRegionsByRegionNameContainingKeyword(String keyword);


    SubRegionDto saveSubRegionWithRegion(Long regionId, String subRegionName);

    List<SubRegionDto> findSubregionByKeyword(String keyword);


}

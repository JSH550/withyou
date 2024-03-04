package com.js.withyou.service;

import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.Region.SubRegionDto;

import java.util.List;

public interface RegionService {
    List<RegionDto> getAllRegions();
    RegionDto getRegionById(Long RegionId);

    RegionDto getRegionByRegionShortName(String RegionShortName);

    List<RegionDto> getRegionByKeyword(String keyword);
    SubRegionDto saveSubRegionWithRegion(Long regionId, String subRegionName);




}

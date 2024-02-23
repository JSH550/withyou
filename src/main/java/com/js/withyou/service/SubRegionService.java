package com.js.withyou.service;

import com.js.withyou.data.dto.MemberDto;
import com.js.withyou.data.dto.Region.SubRegionCreateDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
import com.js.withyou.data.entity.SubRegion;

import java.util.Optional;

public interface SubRegionService {
    SubRegionCreateDto  createSubRegion(SubRegionCreateDto subRegionCreateDto);
    SubRegionCreateDto  updateSubRegion(SubRegionDto subRegionDto);
//    SubRegionCreateDto  deleteSubRegion(Long subRegionId);
    SubRegionCreateDto  getAllSubRegion(SubRegionCreateDto subRegionCreateDto);
    SubRegionCreateDto  getSubRegionById(Long subRegionId);

}

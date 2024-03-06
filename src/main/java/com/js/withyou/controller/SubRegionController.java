package com.js.withyou.controller;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.Region.SubRegionNameDto;
import com.js.withyou.service.RegionService;
import com.js.withyou.service.SubRegionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SubRegionController {
    private final SubRegionService subRegionService;
    private final RegionService regionService;

    public SubRegionController(SubRegionService subRegionService, RegionService regionService) {
        this.subRegionService = subRegionService;
        this.regionService = regionService;
    }

    @ResponseBody
    @GetMapping("/subRegions")
    public List<SubRegionNameDto> getSubRegionNamesByRegionId(@RequestParam(name = "id", required = false) Long regionId) {
        List<SubRegionNameDto> subRegionNameDtoList= regionService.getRegionById((long) regionId).getSubRegions().stream().map(subRegionDto -> {
            SubRegionNameDto subRegionNameDto = new SubRegionNameDto();
            return subRegionNameDto.convertToSubRegionNameDto(subRegionDto.getSubRegionName());
        }).collect(Collectors.toList());

        return subRegionNameDtoList;
    }


}

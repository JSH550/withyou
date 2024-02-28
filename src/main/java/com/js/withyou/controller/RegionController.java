package com.js.withyou.controller;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
import com.js.withyou.data.dto.Region.SubRegionSaveDto;
import com.js.withyou.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/subregions/new")
    public String showAddSubregionForm(Model model) {
        List<RegionDto> allRegions = regionService.getAllRegions();
        SubRegionSaveDto subRegionSaveDto = new SubRegionSaveDto();

        model.addAttribute("regions", allRegions);
        model.addAttribute("subRegionSaveDto", subRegionSaveDto);
        return "/region/add-subRegion-form";


    }

    @ResponseBody
    @PostMapping("/subregions/new")
    public String createNewSubRegion(@ModelAttribute SubRegionSaveDto subRegionSaveDto) {
        String subRegionName = subRegionSaveDto.getSubRegionName();
        log.info("subRegionName={}, regionId={}", subRegionSaveDto.getSubRegionName(),subRegionSaveDto.getRegionId());


        SubRegionDto subRegionDto = regionService.saveSubRegionWithRegion(subRegionSaveDto.getRegionId(), subRegionSaveDto.getSubRegionName());
//        log.info("subRegionId={},subRegionName={}",subRegionDto.getSubRegionId(),subRegionDto.getSubRegionName());

        return "ok";
    }


}

package com.js.withyou.controller;

import com.js.withyou.data.dto.Sigungu.SigunguNameDto;
import com.js.withyou.service.SidoService;
import com.js.withyou.service.SigunguService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SigunguController {
    private final SigunguService sigunguService;
    private final SidoService sidoService;

    public SigunguController(SigunguService sigunguService, SidoService sidoService) {
        this.sigunguService = sigunguService;
        this.sidoService = sidoService;
    }

    @ResponseBody
    @GetMapping("/sigungus")
    public List<SigunguNameDto> getSigunguNamesByRegionId(@RequestParam(name = "id", required = false) Long regionId) {
        List<SigunguNameDto> sigunguNameDtoList = sidoService.getSidoById((long) regionId).getSigungus().stream().map(sigunguDto -> {
            SigunguNameDto sigunguNameDto = new SigunguNameDto();
            return sigunguNameDto.convertToSigunguNameDto(sigunguDto.getSigunguName());
        }).collect(Collectors.toList());

        return sigunguNameDtoList;
    }


}

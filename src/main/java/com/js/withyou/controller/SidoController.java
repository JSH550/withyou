package com.js.withyou.controller;

import com.js.withyou.data.dto.DongNameDto;
import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sido.SidoListDto;
import com.js.withyou.data.dto.Sido.SidoListDtoUtils;
import com.js.withyou.data.dto.Sido.SidoNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.data.dto.Sigungu.SigunguNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguSaveDto;
import com.js.withyou.service.DongService;
import com.js.withyou.service.SidoService;
import com.js.withyou.service.SigunguService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class SidoController {
    private final SidoService sidoService;

    private final SigunguService sigunguService;

    private final DongService dongService;

    public SidoController(SidoService sidoService, SigunguService sigunguService, DongService dongService) {
        this.sidoService = sidoService;
        this.sigunguService = sigunguService;
        this.dongService = dongService;
    }

    @GetMapping("/sigungus/new")
    public String showAddSigunguForm(Model model) {
        List<SidoDto> sidoDtoList = sidoService.getAllSidoList();
        SigunguSaveDto sigunguSaveDto = new SigunguSaveDto();

        model.addAttribute("sido", sidoDtoList);
        model.addAttribute("sigunguSaveDto", sigunguSaveDto);
        return "/region/add-sigungu-form";


    }

    @ResponseBody
    @PostMapping("/sigungus/new")
    public String createNewSigungu(@ModelAttribute SigunguSaveDto sigunguSaveDto) {
        String sigunguName = sigunguSaveDto.getSigunguName();
        log.info("sigunguName={}, sidoId={}", sigunguSaveDto.getSigunguName(), sigunguSaveDto.getSidoId());


        SigunguDto sigunguDto = sidoService.saveSigunguWithSido(sigunguSaveDto.getSidoId(), sigunguSaveDto.getSigunguName());
//        log.info("sigunguId={},sigunguName={}",sigunguDto.getsigunguId(),sigunguDto.getsigunguName());

        return "ok";
    }


    /**
     * 시도(sido) list를 전송합니다.
     * @param model
     * @return
     */
    @GetMapping("/sidos/list")
    public String showSidoListPage(Model model) {

        List<SidoNameDto> allSidoNameDto = sidoService.getAllSidoNameDtoList();

        model.addAttribute("sido", allSidoNameDto);

        return "/place/place-sido";


    }

    /**
     * 지역 정보를 전송하는 API 입니다.
     * 요청 데이터 타입에 따라 다른 테이블을 조회합니다(시도,시군구,읍면동)
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/regions/list")
    public ResponseEntity<List<SidoListDto>> handleRegionListRequest(
            @RequestBody SidoListDto sidoListDto,
            Model model) {
        //request 데이터 타입이 시도(sido)인 경우 해당 시도와 mapping된 시군구(sigungu) 데이터를 반환합니다.
        List<SidoListDto> sidoListDtoList = new ArrayList<>();

        log.info("요청 데이터 ={}", sidoListDto.toString());
        if (sidoListDto.getDataType()== null || sidoListDto.getDataType().isEmpty()){
            List<SidoNameDto> sidoNameDtoList = sidoService.getAllSidoNameDtoList();
            sidoListDtoList = SidoListDtoUtils.convertToSidoListDtoListFromRegion(sidoNameDtoList);
        }
       else if (sidoListDto.getDataType().equals("sido")) {
            List<SigunguNameDto> sigunguNameDtoList = sigunguService.getSigunguNameDtoBySidoId(sidoListDto.getId());
             sidoListDtoList = SidoListDtoUtils.convertToSidoListDtoListFromSigungu(sigunguNameDtoList);
        }
        else if (sidoListDto.getDataType().equals("sigungu")) {
            List<DongNameDto> dongNameDtoList = dongService.getDongNameDtoBySigunguId(sidoListDto.getId());
             sidoListDtoList = SidoListDtoUtils.convertToSidoListDtoListFromDong(dongNameDtoList);
        }else {
            log.info("찾은값 없음");
        }
        for (SidoListDto listDto : sidoListDtoList) {
//            log.info("찾은값 = {}",listDto.toString());
        }

        return ResponseEntity.ok().body(sidoListDtoList);


    }





}

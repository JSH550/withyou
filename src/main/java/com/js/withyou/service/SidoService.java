package com.js.withyou.service;

import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sido.SidoNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SidoService {
    List<SidoDto> getAllSidoList();
    SidoDto getSidoById(Long SidoId);

    SidoDto getSidoDtoBySidoShortName(String sidoShortName);

    List<SidoDto> getSidoDtoListByKeyword(String keyword);

    /**
     * keyword로 지역(Sido)을 검색후 SidoNameDto를 (Id와 지역명만 저장) List형태로 반환합니다.
     * @param keyword
     * @return SidoNameDto
     */
    @Transactional
    List<SidoNameDto> getSidoNameDtoListByKeyword(String keyword);

    List<SigunguDto> getSigungusBySidoNameContainingKeyword(String keyword);


    SigunguDto saveSigunguWithSido(Long sidoId, String sigunguName);

    List<SigunguDto> findSigunguByKeyword(String keyword);


    /**
     * 모든 시도(sido) 레코드를 sidoNameDto로 반환합니다.
     * @return SidoNameDto PK값과 이름값만 갖고있습니다.
     */
    List<SidoNameDto> getAllSidoNameDtoList();






}

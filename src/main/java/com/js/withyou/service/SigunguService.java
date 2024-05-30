package com.js.withyou.service;

import com.js.withyou.data.dto.Sigungu.SigunguNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguSearchDto;
import com.js.withyou.data.entity.Region.Sigungu;

import java.util.List;

public interface SigunguService {


    Sigungu getSigunguBySidoNameAndSigunguName(String sidoName, String sigunguName);

    List<Long> findSigunguIdsByKeyword(String searchKeyword);


    /**
     *
     * @param keyword
     * @return SigunguSearchDto sigungu Id와 sidoName과 결합된 문자열 입니다.(예: 경기도 + 수원시 영통구, "경기도 수원시 영통구)
     */
    List<SigunguSearchDto> getSigunguSearchDtosByKeyword(String keyword);

    /**
     * 시도(sido) id로 시군구(sigungu) 데이터를 반환하븐 메서드입니다.
     * @param sidoId
     * @return SigunguNameDto sigungu  id와 이름을 갖는 DTO입니다.
     */
    List<SigunguNameDto> getSigunguNameDtoBySidoId(Long sidoId);


    /**
     * 시도 Id값과 시군구 이름으로 시군구 정보를 찾는 메서드입니다.
     * @param sidoId
     * @param sigunguName
     * @return
     */
    SigunguNameDto getSigunguNameDtoBySidoIdAndSigunguName(Long sidoId, String sigunguName);



}
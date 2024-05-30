package com.js.withyou.service;

import com.js.withyou.data.dto.DongNameDto;

import java.util.List;

public interface DongService {

    /**
     * 시군구(sigungu) PK로 mapping된 읍면동(dong) 레코드를 찾아 반환하는 메서드입니다.
     * @param sigunguId 시군구(sigungu) PK입니다.
     * @return DongNameDto List로 반환합니다.
     */
    List<DongNameDto> getDongNameDtoBySigunguId(Long sigunguId);

    DongNameDto getDongNameDtoBySigunguIdAndDongName(Long sigunguId, String dongName);


}

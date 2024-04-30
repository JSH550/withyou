package com.js.withyou.service;

import com.js.withyou.data.dto.DongRegionNameDto;
import com.js.withyou.data.entity.Region.DongRegion;
import com.js.withyou.repository.DongRegionRepository;

import java.util.List;

public interface DongRegionService {

    /**
     * 시군구(subRegion) PK로 mapping된 읍면동(dongRegion) 레코드를 찾아 반환하는 메서드입니다.
     * @param subRegionId 시군구(subRegion) PK입니다.
     * @return DongRegionNameDto List로 반환합니다.
     */
    List<DongRegionNameDto> getDongRegionNameDtoBySubRegionId(Long subRegionId);

}

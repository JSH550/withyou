package com.js.withyou.service.impl;

import com.js.withyou.data.dto.DongRegionNameDto;
import com.js.withyou.data.dto.SubRegion.SubRegionNameDto;
import com.js.withyou.data.entity.Region.DongRegion;
import com.js.withyou.data.entity.Region.SubRegion;
import com.js.withyou.repository.DongRegionRepository;
import com.js.withyou.service.DongRegionService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DongRegionServiceImpl implements DongRegionService {

    private final    DongRegionRepository dongRegionRepository;

    public DongRegionServiceImpl(DongRegionRepository dongRegionRepository) {
        this.dongRegionRepository = dongRegionRepository;
    }

    public List<DongRegionNameDto> getDongRegionNameDtoBySubRegionId(Long subRegionId) {
        List<DongRegion> dongRegionList = dongRegionRepository.findDongRegionsBySubRegionSubRegionId(subRegionId);
        List<DongRegionNameDto> regionNameDtoList = dongRegionList.stream()
                .map(DongRegion -> new DongRegionNameDto().convertToDontRegionNameDto(DongRegion)).collect(Collectors.toList());
        return regionNameDtoList;
//        subRegionRepository.find


    }

}

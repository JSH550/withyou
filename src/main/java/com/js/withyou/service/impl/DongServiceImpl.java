package com.js.withyou.service.impl;

import com.js.withyou.data.dto.DongNameDto;
import com.js.withyou.data.entity.Region.Dong;
import com.js.withyou.repository.DongRepository;
import com.js.withyou.service.DongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DongServiceImpl implements DongService {

    private final DongRepository dongRepository;

    public DongServiceImpl(DongRepository dongRepository) {
        this.dongRepository = dongRepository;
    }

    public List<DongNameDto> getDongNameDtoBySigunguId(Long sigunguId) {
        List<Dong> dongList = dongRepository.findDongsBySigunguSigunguId(sigunguId);
        List<DongNameDto> dongNameDtoList = dongList.stream()
                .map(Dong -> new DongNameDto().convertToDongNameDto(Dong))
                .collect(Collectors.toList());
        return dongNameDtoList;
//        sigunguRepository.find


    }

    @Override
    public DongNameDto getDongNameDtoBySigunguIdAndDongName(Long sigunguId, String dongName) {
        Optional<Dong> dong = dongRepository.findBySigunguIdAndDongName(sigunguId, dongName);

        DongNameDto dongNameDto = new DongNameDto();
        if (dong.isPresent()){
          return   dongNameDto.convertToDongNameDto(dong.get());
        }

        return dongNameDto;
    }

}

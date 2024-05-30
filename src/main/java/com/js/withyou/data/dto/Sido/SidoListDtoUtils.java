package com.js.withyou.data.dto.Sido;

import com.js.withyou.data.dto.DongNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguNameDto;

import java.util.List;
import java.util.stream.Collectors;

public class SidoListDtoUtils {
    private SidoListDtoUtils() {
        // 생성자를 private으로 선언하여 외부에서 인스턴스화되는 것을 방지합니다.
    }


    public static List<SidoListDto> convertToSidoListDtoListFromRegion(List<SidoNameDto> sidoNameDtoList) {
        return sidoNameDtoList.stream()
                .map(sidoNameDto -> SidoListDto.builder()
                        .id(sidoNameDto.getSidoId())
                        .name(sidoNameDto.getSidoName())
                        .dataType("sido").build())
                .collect(Collectors.toList());
    }

    public static List<SidoListDto> convertToSidoListDtoListFromSigungu(List<SigunguNameDto> sigunguNameDtoList) {
        return sigunguNameDtoList.stream()
                .map(sigunguNameDto -> SidoListDto.builder()
                        .id(sigunguNameDto.getId())
                        .name(sigunguNameDto.getSigunguName())
                        .dataType("sigungu").build())
                .collect(Collectors.toList());
    }

    public static List<SidoListDto> convertToSidoListDtoListFromDong(List<DongNameDto> dongNameDtoList) {
        return dongNameDtoList.stream()
                .map(dongNameDto -> SidoListDto.builder()
                        .id(dongNameDto.getDongId())
                        .name(dongNameDto.getDongName())
                        .dataType("dong").build())
                .collect(Collectors.toList());
    }
}

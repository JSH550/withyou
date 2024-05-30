package com.js.withyou.data.dto.Sido;

import com.js.withyou.data.dto.Sigungu.SigunguDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class SidoDto {

    @NotBlank
    private Long sidoId;


    @NotBlank
    private String sidoName;

    private String sidoShortName; // 시도명의 줄임말 필드,ex) 충남 충북


    private List<SigunguDto> sigungus;

}

package com.js.withyou.data.dto.Sigungu;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * 추천 검색어에 쓰일 DTO 입니다.
 * Id 값과 시도(region)이름과 결합된 sigunguNameLong을 갖습니다.
 * 예를들어 수원시 영통구는 "경기도 수원시 영통구" 형태로 저장합니다.
 */
@Getter
@ToString
@Builder
public class SigunguSearchDto {

    @NotNull
    Long sigunguId;

    @NotNull
    String sigunguNameLong;


}

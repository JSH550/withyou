package com.js.withyou.data.dto.Sido;


import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
//지역 선택시 사용될 DTo입니다.
public class SidoListDto {

    //PK입니다.
    private Long id;
    //데이터 타입입니다.  sigungu dong 3종류 입니다.
    private String dataType;

    //레코드 이름입니다.
    private String name;

}

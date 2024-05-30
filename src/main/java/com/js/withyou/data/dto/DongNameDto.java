package com.js.withyou.data.dto;

import com.js.withyou.data.entity.Region.Dong;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DongNameDto {

    private Long dongId;

    private String dongName;


public DongNameDto convertToDongNameDto(Dong dong){
    return new DongNameDto().builder()
            .dongId(dong.getDongId())
            .dongName(dong.getDongName())
            .build();
}
}

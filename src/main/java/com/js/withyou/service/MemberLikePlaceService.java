package com.js.withyou.service;

import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.MemberLikePlace;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberLikePlaceService {



    String saveMemberLikePlace(Long placeId, String memberEmail);

    /**
     *
     * @param placeId
     * @param memberEmail
     * @return
     */
    boolean checkMemberLikePlaceByPlaceAndMemberExistence(Long placeId, String memberEmail);


    List<MemberLikePlace> getMemberLikePlaceEntityByMemberEmail(String memberEmail);
    String deleteMemberLikePlace(Long placeId, String memberEmail);

    @Transactional
    List<PlaceDto> getPlaceDtosByMemberEmail(String memberEmail);

//    @Transactional
//    List<PlaceDto> getPlaceDtosByMemberEmail(String memberEmail);
}

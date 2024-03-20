package com.js.withyou.service.impl;

import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.entity.MemberLikePlace;
import com.js.withyou.repository.MemberLikePlaceRepository;
import com.js.withyou.service.MemberLikePlaceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemberLikePlaceServiceImplTest {

    private final MemberLikePlaceService memberLikePlaceService;
    private final MemberLikePlaceRepository memberLikePlaceRepository;


@Autowired
    public MemberLikePlaceServiceImplTest(MemberLikePlaceService memberLikePlaceService, MemberLikePlaceRepository memberLikePlaceRepository) {
        this.memberLikePlaceService = memberLikePlaceService;

    this.memberLikePlaceRepository = memberLikePlaceRepository;
}


//    save
    @Test
    public void saveUserFavoritePlaceTest() {
        memberLikePlaceService.saveMemberLikePlace(2270L,"kim@gmail.com");
    }

    @Test
    public void getUserFavoritePlaceByPlaceAndMember(){
        boolean result = memberLikePlaceService.checkMemberLikePlaceByPlaceAndMemberExistence(2274L, "kim@gmail.com");
        Assertions.assertThat(result).isEqualTo("OK");
        System.out.println(result);
    }

    @Test
    public void deleteUserFavoritePlaceTest(){
    memberLikePlaceService.deleteMemberLikePlace(2270L,"kim@gmail.com");


    }

    @Test
    public void getMemberLikePlaceEntityByMemberEmailTest(){

        List<MemberLikePlace> memberLikePlaceEntityByMemberEmail = memberLikePlaceService.getMemberLikePlaceEntityByMemberEmail("kim2@gmail.com");
        for (MemberLikePlace memberLikePlace : memberLikePlaceEntityByMemberEmail) {
            System.out.println(memberLikePlace.getId());
        }

    }






}
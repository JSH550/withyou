package com.js.withyou.service.impl;

import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.UserFavoritePlace;
import com.js.withyou.repository.UserFavoritePlaceRepository;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.UserFavoritePlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserFavoritePlaceServiceImpl implements UserFavoritePlaceService {

    private final UserFavoritePlaceRepository userFavoritePlaceRepository;

    private final MemberService memberService;
    private final PlaceService placeService;
    @Autowired
    public UserFavoritePlaceServiceImpl(UserFavoritePlaceRepository userFavoritePlaceRepository, MemberService memberService, PlaceService placeService) {
        this.userFavoritePlaceRepository = userFavoritePlaceRepository;
        this.memberService = memberService;
        this.placeService = placeService;

    }


    /**
     * 유저가 좋아요 누른 place 정보를 저장하는 메서드 입니다.
     * @param placeId 좋아요가 눌린 시설(place)의 FK 입니다.
     * @param memberEmail 좋아요를 누른 유저(member)의 Email 입니다.
     */
    @Transactional
    @Override
    public void saveUserFavoritePlace(Long placeId, String memberEmail) {
        Place foundPlace = placeService.getPlaceEntityByPlaceId(placeId);//파라미터로 전달받은 placId로 place Entity를 검색합니다
        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.
        if (foundPlace==null||foundMember==null){
            throw new NoSuchElementException("시설 정보 또는 유저 정보가 없습니다.");//place 또는 member 정보가 없으면 예외로 던집니다.
        }
        UserFavoritePlace userFavoritePlace = new UserFavoritePlace(foundMember,foundPlace);
        UserFavoritePlace savedUserFavoritePlace = userFavoritePlaceRepository.save(userFavoritePlace);

        if (savedUserFavoritePlace==null){
            throw new IllegalStateException("저장오류 : 저장된 UserFavoritePlace가 null입니다.");//저장에 실패했을경우 예외로 던집니다.
        }
        log.info("저장된 userFavoritePlace={}",savedUserFavoritePlace.getId());
    }

    @Transactional
    @Override
    public void getUserFavoritePlaceByPlaceAndMember(Long placeId, String memberEmail) {

        Place foundPlace = placeService.getPlaceEntityByPlaceId(placeId);//파라미터로 전달받은 placId로 place Entity를 검색합니다
        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.

        UserFavoritePlace foundUserFavoritePlace = userFavoritePlaceRepository.findUserFavoritePlaceByPlaceAndMember(placeId, foundMember.getMemberId());
        log.info("찾은정보={}",foundUserFavoritePlace.getId());



    }

    @Transactional
    @Override
    public void deleteUserFavoritePlace(Long placeId, String memberEmail){
        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.

        userFavoritePlaceRepository.deleteUserFavoritePlaceByPlaceAndMember(placeId,memberEmail);


    }



}



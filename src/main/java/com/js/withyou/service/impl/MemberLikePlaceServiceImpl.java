package com.js.withyou.service.impl;

import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.MemberLikePlace;
import com.js.withyou.repository.MemberLikePlaceRepository;
import com.js.withyou.service.MemberService;
import com.js.withyou.service.PlaceService;
import com.js.withyou.service.MemberLikePlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberLikePlaceServiceImpl implements MemberLikePlaceService {

    private final MemberLikePlaceRepository memberLikePlaceRepository;

    private final MemberService memberService;
    private final PlaceService placeService;
    @Autowired
    public MemberLikePlaceServiceImpl(MemberLikePlaceRepository memberLikePlaceRepository, MemberService memberService, PlaceService placeService) {
        this.memberLikePlaceRepository = memberLikePlaceRepository;
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
    public String saveMemberLikePlace(Long placeId, String memberEmail) {
        Place foundPlace = placeService.getPlaceEntityByPlaceId(placeId);//파라미터로 전달받은 placId로 place Entity를 검색합니다
        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.
        if (foundPlace==null||foundMember==null){
            throw new NoSuchElementException("시설 정보 또는 유저 정보가 없습니다.");//place 또는 member 정보가 없으면 예외로 던집니다.
        }
        MemberLikePlace memberLikePlace = new MemberLikePlace(foundMember,foundPlace);
        MemberLikePlace savedMemberLikePlace = memberLikePlaceRepository.save(memberLikePlace);

        if (savedMemberLikePlace ==null){
            throw new IllegalStateException("저장오류 : 저장된 MemberLikePlace가 null입니다.");//저장에 실패했을경우 예외로 던집니다.
        }

        log.info("저장된 memberLikePlace={}", savedMemberLikePlace.getId());
        return "OK";
    }



    @Transactional
    @Override
    public String deleteMemberLikePlace(Long placeId, String memberEmail){
//        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.

        memberLikePlaceRepository.deleteMemberLikePlaceByPlaceAndMember(placeId,memberEmail);

        return "OK";

    }




    /**
     * 시설 PK(placeId)와 유저Email(memberEmail)로 좋아요 정보를 검색합니다.
     *
     * @param placeId
     * @param memberEmail
     * @ToDo 쿼리문 최적화
     */
    @Transactional
    @Override

    public boolean checkMemberLikePlaceByPlaceAndMemberExistence(Long placeId, String memberEmail) {

//        Place foundPlace = placeService.getPlaceEntityByPlaceId(placeId);//파라미터로 전달받은 placId로 place Entity를 검색합니다
//        Member foundMember = memberService.getMemberEntityByEmail(memberEmail);//파라미터로 전달받은 email로 member Entity를 검색합니다.

        MemberLikePlace foundMemberLikePlace = memberLikePlaceRepository.findByPlaceAndMember(placeId, memberEmail);
        return foundMemberLikePlace !=null;

    }

    @Override
    public List<MemberLikePlace> getMemberLikePlaceEntityByMemberEmail(String memberEmail) {
        List<MemberLikePlace> memberLikePlaceList = memberLikePlaceRepository.findByMemberEmail(memberEmail);
        return memberLikePlaceList;
    }

    @Transactional
    @Override
    public List<PlaceDto> getPlaceDtosByMemberEmail(String memberEmail) {
        List<MemberLikePlace> memberLikePlaceEntityByMemberEmail = getMemberLikePlaceEntityByMemberEmail(memberEmail);
        List<PlaceDto> placeDtos = memberLikePlaceEntityByMemberEmail.stream().map(memberLikePlace -> {
            PlaceDto placeDto = new PlaceDto();
            return placeDto.convertToPlaceDto(memberLikePlace.getPlace());
        }).collect(Collectors.toList());

        return placeDtos;
//        return null;
    }

    //멤버likeplace list에서 place id만 저장해주세용~





}



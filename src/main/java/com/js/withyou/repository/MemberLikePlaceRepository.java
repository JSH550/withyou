package com.js.withyou.repository;


import com.js.withyou.data.entity.MemberLikePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MemberLikePlaceRepository extends JpaRepository<MemberLikePlace, Long> {

    @Query("SELECT m " +
            "FROM MemberLikePlace m " +
            "WHERE m.place.placeId = :placeId AND " +
            "m.member.memberEmail = :memberEmail")
    MemberLikePlace findMemberLikePlaceByPlaceAndMember(@Param("placeId")Long placeId, @Param("memberEmail")String memberEmail);

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberLikePlace m " +
            "WHERE m.place.placeId = :placeId AND " +
            "m.member.memberEmail = :memberEmail")
    void deleteMemberLikePlaceByPlaceAndMember(@Param("placeId") Long placeId, @Param("memberEmail")String MemberEmail);


}

package com.js.withyou.repository;


import com.js.withyou.data.entity.UserFavoritePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserFavoritePlaceRepository extends JpaRepository<UserFavoritePlace, Long> {

    @Query("SELECT f FROM UserFavoritePlace f " +
            "WHERE f.place.placeId = :placeId AND " +
            "f.member.memberId = :memberId")
    UserFavoritePlace findUserFavoritePlaceByPlaceAndMember(Long placeId, Long memberId);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserFavoritePlace f " +
            "WHERE f.place.placeId = :placeId AND " +
            "f.member.memberEmail = :memberEmail")
    void deleteUserFavoritePlaceByPlaceAndMember(@Param("placeId") Long placeId, @Param("memberEmail")String MemberEmail);


}

package com.js.withyou.service;

import com.js.withyou.data.entity.Member;
import com.js.withyou.data.entity.Place.Place;

public interface UserFavoritePlaceService {



    void saveUserFavoritePlace(Long placeId, String memberEmail);

    void getUserFavoritePlaceByPlaceAndMember(Long placeId, String memberEmail);

    void deleteUserFavoritePlace(Long placeId, String memberEmail);
}

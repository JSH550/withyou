package com.js.withyou.data.entity.Place;

import com.js.withyou.data.dto.place.PlaceCreateDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Region.DongRegion;
import com.js.withyou.data.entity.Review;
import com.js.withyou.data.entity.Region.SubRegion;
import com.js.withyou.data.entity.MemberLikePlace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
//@Builder
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(nullable = false)
    private String placeName;

    private String placeRoadAddress;

    private double placeLatitude;//위도 Y축

    private double placeLongitude;//경도 X축

    //카테고리에 매핑
    @ManyToOne()
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

//    //시군구에 maping
//    @ManyToOne
//    @JoinColumn(name = "subregion_id", nullable = false)
//    private SubRegion subRegion;

    //시군구에 maping
    @ManyToOne
    @JoinColumn(name = "dongRegion_id", nullable = false)
    private DongRegion dongRegion;

    //시설의 review와 mapping
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    //유저가 좋아요 누른 장소, 중간 테이블과 mapping
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberLikePlace> favoritePlaces;

//    //단방향 mapping으로 주소 참조
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "address_id")
//    private Address address;

//    public Place createPlace(String placeName
//            , String placeRoadAddress
//            , double placeLatitude
//            , double placeLongitude
//            , Category category
//            , SubRegion subRegion) {
//        Place place = new Place();
//        place.placeName = placeName;
//        place.placeRoadAddress = placeRoadAddress;
//        place.placeLatitude = placeLatitude;
//        place.placeLongitude =placeLongitude;
//        place.category =category;
//        place.subRegion=subRegion;
//        return place;
//    }

    public Place createPlace(PlaceCreateDto placeCreateDto
            , Category category
            , SubRegion subRegion) {
        Place place = new Place();
        place.placeName = placeCreateDto.getPlaceName();
        place.placeRoadAddress = placeCreateDto.getPlaceRoadAddress();
        place.placeLatitude = placeCreateDto.getPlaceLatitude();
        place.placeLongitude = placeCreateDto.getPlaceLongitude();
        place.category = category;
        return place;
    }

    public Place createPlace(Long placeId) {
        Place place = new Place();
        this.placeId = placeId;
        return place;
    }

//    public Place convertToPlaceEntity(PlaceDto placeDto){
//    return Place.builder().placeName(placeDto.getPlaceName())
//            .placeRoadAddress(placeDto.getPlaceRoadAddress())
//            .placeLongitude(placeDto.getPlaceLongitude())
//            .placeLatitude(placeDto.getPlaceLatitude())
//            .build();
//
//
//    }


}

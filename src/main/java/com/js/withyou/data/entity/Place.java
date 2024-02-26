package com.js.withyou.data.entity;

import com.js.withyou.data.dto.PlaceSaveDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    //시군구에 maping
    @ManyToOne
    @JoinColumn(name = "subregion_id", nullable = false)
    private SubRegion subRegion;

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

    public Place createPlace(PlaceSaveDto placeSaveDto
            , Category category
            , SubRegion subRegion) {
        Place place = new Place();
        place.placeName = placeSaveDto.getPlaceName();
        place.placeRoadAddress = placeSaveDto.getPlaceRoadAddress();
        place.placeLatitude = placeSaveDto.getPlaceLatitude();
        place.placeLongitude =placeSaveDto.getPlaceLongitude();
        place.category =category;
        place.subRegion=subRegion;
        return place;
    }


}

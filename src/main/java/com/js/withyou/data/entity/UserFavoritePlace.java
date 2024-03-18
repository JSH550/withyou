package com.js.withyou.data.entity;


import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class UserFavoritePlace {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    public UserFavoritePlace(Member member, Place place) {
        this.member = member;
        this.place = place;
    }
}




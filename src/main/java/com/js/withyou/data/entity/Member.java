package com.js.withyou.data.entity;

import com.js.withyou.data.entity.Place.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment 자동생성
    @Column(name = "member_id")
    private Long memberId;

    @Column(unique = true)
    private String memberEmail;

    private String memberPassword;
    //이름
    private String memberName;
    
    //회원이 작성한 리뷰
    @OneToMany
    private List<Review> reviews;
    
    //meber 저장 메서드
    public void saveMember(String memberEmail,String memberName,String memberPassword){
        this.memberEmail=memberEmail;
        this.memberName=memberName;
        this.memberPassword=memberPassword;
    }
}

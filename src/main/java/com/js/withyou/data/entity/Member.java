package com.js.withyou.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
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
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,fetch = LAZY)
    private List<Review> reviews = new ArrayList<>();

    //좋아요 누른 시설
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,fetch = LAZY)
    private List<MemberLikePlace> memberLikePlace = new ArrayList<>();


    /**
     * 유저의 닉네임을 변경하기 위한 수정 메서드입니다.
     * @param updateName
     */
    public void updateMemberName(String updateName){
        this.memberName = updateName;
    }


    /**
     * 유저의 닉네임을 변경하기 위한 수정 메서드입니다.
     * @param password 인코딩된 password 입니다.
     */
    public void updateMemberPassword(String password){
        this.memberPassword = password;
    }


    
    //meber 저장 메서드
    public void saveMember(String memberEmail,String memberName,String memberPassword){
        this.memberEmail=memberEmail;
        this.memberName=memberName;
        this.memberPassword=memberPassword;
    }
}

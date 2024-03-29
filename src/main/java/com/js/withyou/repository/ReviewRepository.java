package com.js.withyou.repository;

import com.js.withyou.data.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.member.memberEmail = :memberEmail ")
    List<Review> findReviewByMemberEmail(@Param("memberEmail")String memberEmail);

}

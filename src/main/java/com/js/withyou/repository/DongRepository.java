package com.js.withyou.repository;

import com.js.withyou.data.entity.Region.Dong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DongRepository extends JpaRepository<Dong, Long> {


    List<Dong> findDongsBySigunguSigunguId(Long sigunguId);

    @Query("select d from Dong d " +
            "where d.sigungu.sigunguId = :sigunguId and " +
            "d.dongName = :dongName")
    Optional<Dong> findBySigunguIdAndDongName(@Param("sigunguId") Long sigunguId,
                                              @Param("dongName") String dongName);


}

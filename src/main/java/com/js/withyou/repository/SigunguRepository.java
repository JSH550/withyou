package com.js.withyou.repository;

import com.js.withyou.data.entity.Region.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SigunguRepository extends JpaRepository<Sigungu,Long> {

    Optional<Sigungu> findBySigunguName(String sigunguName);

    /**
     * 주어진 키워드를 포함하는 하위 지역(Sigungu)을 데이터베이스에서 검색합니다.
     * 검색된 하위 지역은 지역(Sido) 엔티티와의 조인(fetch join)을 통해 함께 로드됩니다.
     * @param keyword 검색에 사용할 키워드
     * @return 키워드를 포함하는 하위 지역(Sigungu)의 목록을 반환합니다.
     */
    @Query("SELECT s " +
            "FROM Sigungu s " +
            "JOIN FETCH s.sido " +
            "WHERE s.sigunguName LIKE %:keyword%")
    List<Sigungu> findBySigunguNameContaining(@Param("keyword") String keyword) ;



    @Query("select s " +
            "from Sigungu s " +
//            "Join s.sido si " +
            "Where s.sido.sidoId =:sidoId and " +
            "s.sigunguName = :sigunguName")
    Optional<Sigungu> findBySidoIdAndSigunguName(@Param("sidoId") Long sidoId,
                                                 @Param("sigunguName")String sigunguName);


    List<Sigungu> findSigunguBySidoSidoId(@Param("sidoId") Long sidoId);



}

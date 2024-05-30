package com.js.withyou.repository;

import com.js.withyou.data.entity.Region.Sido;
import com.js.withyou.data.entity.Region.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SidoRepository extends JpaRepository<Sido,Long> {

    /**
     * 시도(sido)의 shortName으로 시도 엔티티를 찾습니다.(shortName 예시 :충남, 충북)
     * @param sidoShortName
     * @return
     */
    Optional<Sido> findSidoBySidoShortName(String sidoShortName);
    @Query("SELECT DISTINCT r FROM Sido r " +
            "LEFT JOIN FETCH r.sigungus " +//JOIN FETCH로 sigungu 컬렉션을 함께 가져옴
            "WHERE r.sidoName LIKE %:keyword%")
    List<Sido> findSidoListWithSigungusAndPlacesByNameContaining(String keyword);

    /**
     *
     * @param keyword
     * @return
     */
    @Query("SELECT DISTINCT r FROM Sido r " +
            "LEFT JOIN FETCH r.sigungus " +
            "where r.sidoShortName LIKE %:keyword%")
    List<Sido> findSidoBySidoShortNameContaining(String keyword);



    @Query("SELECT DISTINCT r FROM Sido r " +
            "LEFT JOIN FETCH r.sigungus " +
            "where  r.sidoName like %:keyword% or r.sidoShortName LIKE %:keyword%")
    List<Sido> findByKeywordContaining(@Param("keyword") String keyword);


    @Query("SELECT r.sigungus " +
            "FROM Sido r " +
            "WHERE r.sidoName Like %:keyword%")
    List<Sigungu> findSigungusBySidoNameContaining(@Param("keyword") String keyword);


    /**
     *
     * @param sidoId
     * @return
     */
    @Query("select sr from Sigungu sr " +
            "where sr.sido.sidoName =:sidoId")
    List<Sigungu> findSigunguBySidoId(@Param("sidoId") Long sidoId);




}

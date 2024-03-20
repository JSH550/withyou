package com.js.withyou.repository;

import com.js.withyou.data.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubRegionRepository extends JpaRepository<SubRegion,Long> {

    Optional<SubRegion> findBySubRegionName(String subRegionName);

    /**
     * 주어진 키워드를 포함하는 하위 지역(SubRegion)을 데이터베이스에서 검색합니다.
     * 검색된 하위 지역은 지역(Region) 엔티티와의 조인(fetch join)을 통해 함께 로드됩니다.
     * @param keyword 검색에 사용할 키워드
     * @return 키워드를 포함하는 하위 지역(SubRegion)의 목록을 반환합니다.
     */
    @Query("SELECT s " +
            "FROM SubRegion s " +
            "JOIN FETCH s.region " +
            "WHERE s.subRegionName LIKE %:keyword%")
    List<SubRegion> findBySubRegionNameContaining(@Param("keyword") String keyword) ;
//    Optional<SubRegion> findByRegionIdAndSubRegionName(Long regionId,String subRegionName);
//    Optional<SubRegion> findByRegion_RegionIdAndSubRegionName(Long regionId, String subRegionName);





}

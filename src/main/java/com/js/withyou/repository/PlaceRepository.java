package com.js.withyou.repository;

import com.js.withyou.data.entity.Place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    /**
     * 장소명에 특정 키워드를 포함하는 장소를 조회합니다.
     * 이 메서드는 JOIN FETCH로, 관련된 SubRegion과
     * Category를 함께 즉시 로딩합니다.
     *
     * @param keyword 조회할 장소명에 포함된 키워드
     * @return 장소명에 특정 키워드를 포함하는 장소의 목록
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.subRegion " +
            "JOIN FETCH p.category "+
            "WHERE p.placeName LIKE %:keyword%")
    List<Place> findByPlaceNameContaining(@Param("keyword") String keyword);

    @Query("SELECT p FROM Place p Where p.subRegion.subRegionId = :subregionId")
    List<Place> findBySubregionId(Long subregionId);



    /**
     * 특정 시도(region)과 관계가 있는 시군구(subRegion)에 관계가 있는 시설(Place)를 찾아 반환합니다.
     * @param regionId regionId 찾고자 하는 시도(region)의 식별자
     * @return 특정 시도(region)과 관계가 있는 시군구(subRegion)에 관계가 있는 시설(Place)의 목록
     */
    @Query("SELECT p from Place p " +
            "where p.subRegion IN " +
            "(select sr from SubRegion sr " +
            "where sr.region.regionId = :regionId)")
    List<Place> findPlacesByRegionId(@Param("regionId") Long regionId);


}

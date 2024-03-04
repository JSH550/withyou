package com.js.withyou.repository;

import com.js.withyou.data.entity.Place;
import com.js.withyou.data.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    /**
     * 장소명에 특정 키워드를 포함하는 장소를 조회합니다.
     * 이 메서드는 JOIN FETCH로, 관련된 SubRegion과 Category를 함께 즉시 로딩합니다.
     *
     * @param keyword 조회할 장소명에 포함된 키워드
     * @return 장소명에 특정 키워드를 포함하는 장소의 목록
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.subRegion " +
            "JOIN FETCH p.category "+
            "WHERE p.placeName LIKE %:keyword%")
    List<Place> findByPlaceNameContaining(String keyword);

    @Query("SELECT p FROM Place p Where p.subRegion.subRegionId = :subregionId")
    List<Place> findBySubregionId(Long subregionId);


}

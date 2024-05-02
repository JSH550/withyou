package com.js.withyou.repository;

import com.js.withyou.data.entity.Place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "JOIN FETCH p.category " +
            "WHERE p.placeName LIKE %:keyword%")
    List<Place> findByPlaceNameContaining(@Param("keyword") String keyword);

//    @Query("SELECT p FROM Place p Where p.subRegion.subRegionId = :subregionId")
//    List<Place> findBySubregionId(Long subregionId, Pageable pageable);
//
//    @Query("SELECT p FROM Place p Where p.subRegion.subRegionId = :subregionId")
//    Page<Place> findPageBySubregionId(Long subregionId, Pageable pageable);


    List<Place> findByPlaceNameContaining(String keyword, Pageable pageable);


//    Page<Place> findByNameContaining(String keyword, Pageable pageable);

//    Page<Place> findPlaceByPlaceNameContainsAndPageable(String keyword, Pageable pageable);


    /**
     * 특정 읍면동(dongRegion) 지역 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param dongRegionId
     * @param pageable
     * @return
     */
    @Query("select p from Place p " +
            "Where p.dongRegion.dongRegionId = :dongRegionId")
    Page<Place> findPlacesByDongRegionId(@Param("dongRegionId") Long dongRegionId, Pageable pageable);

    /**
     * 특정 읍면동(dongRegion) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param dongRegionId 특정 동 지역의 ID값
     * @param categoryId     장소의 카테고리 ID값
     * @param pageable     페이지네이션 정보
     * @return 해당 동 지역에 속하고 지정된 카테고리에 해당하는 장소 목록
     */
    @Query("select p from Place p " +
            "JOIN FETCH p.category c " +
            "Where p.dongRegion.dongRegionId = :dongRegionId " +
            "and p.category.categoryId = :categoryId")
    Page<Place> findPlacesByDongRegionIdAndCategoryId(@Param("dongRegionId") Long dongRegionId,
                                         @Param("categoryId") Long categoryId,
                                         Pageable pageable);

    /**
     * 특정 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param categoryId
     * @param pageable
     * @return
     */
    @Query("select p from Place p " +
            "where p.category.categoryId = :categoryId")
    Page<Place> findPlacesByCategoryId(@Param("categoryId") Long categoryId,
                                       Pageable pageable);


    /**
     * subRegion id로 해당하는 DongRegion에 매핑된 Place를 찾는 메서드
     *
     * @param subRegionId
     * @return
     */
    @Query("SELECT p FROM Place p WHERE p.dongRegion IN " +
            "(SELECT d FROM DongRegion d WHERE d.subRegion.subRegionId = :subRegionId)")
    Page<Place> findPlacesBySubRegionId(@Param("subRegionId") Long subRegionId, Pageable pageable);


    /**
     * 특정 시군구(subRegion) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param subRegionId
     * @param categoryId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p WHERE p.dongRegion IN " +
            "(SELECT d FROM DongRegion d WHERE d.subRegion.subRegionId = :subRegionId) and " +
            "p.category.categoryId =:categoryId")
    Page<Place> findPlacesBySubRegionIdAndCategoryId(@Param("subRegionId") Long subRegionId,
                                                      @Param("categoryId") Long categoryId,
                                                      Pageable pageable);

    /**
     * @param regionId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "WHERE p.dongRegion IN " +
            "(SELECT d FROM DongRegion d WHERE d.subRegion IN " +
            "(SELECT s FROM SubRegion s Where s.region.regionId = :regionId))")
    Page<Place> findPlacesByRegionId(@Param("regionId") Long regionId, Pageable pageable);

    /**
     *
     *
     * 특정 시도(region) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param regionId
     * @param categoryId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.category c " +
            "WHERE p.dongRegion IN " +
            "(SELECT d FROM DongRegion d WHERE d.subRegion IN " +
            "(SELECT s FROM SubRegion s Where s.region.regionId = :regionId)) and " +
            "p.category.categoryId =:categoryId")
    Page<Place> findPlacesByRegionIdAndCategoryId(@Param("regionId") Long regionId,
                                                  @Param("categoryId") Long categoryId,
                                                  Pageable pageable);


    /**
     * 특정 시도(region)과 관계가 있는 시군구(subRegion)에 관계가 있는 시설(Place)를 찾아 반환합니다.
     * @param regionId regionId 찾고자 하는 시도(region)의 식별자
     * @param pageable 페이지네이션을 위한 객체
     * @return 특정 시도(region)과 관계가 있는 시군구(subRegion)에 관계가 있는 시설(Place)의 목록
     */
//    @Query("SELECT p from Place p " +
//            "where p.subRegion IN " +
//            "(select sr from SubRegion sr " +
//            "where sr.region.regionId = :regionId)")
//    Page<Place> findPlacesByRegionId(@Param("regionId") Long regionId, Pageable pageable);


}

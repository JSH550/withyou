package com.js.withyou.repository;

import com.js.withyou.data.entity.Place.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    /**
     * 장소명에 특정 키워드를 포함하는 장소를 조회합니다.
     * 이 메서드는 JOIN FETCH로, 관련된 sigungu과
     * Category를 함께 즉시 로딩합니다.
     *
     * @param keyword 조회할 장소명에 포함된 키워드
     * @return 장소명에 특정 키워드를 포함하는 장소의 목록
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.category " +
            "WHERE p.placeName LIKE %:keyword%")
    List<Place> findByPlaceNameContaining(@Param("keyword") String keyword);


    @Query("select p from Place p " +
            "join fetch p.category " +
            "where p.placeName like %:searchWord%")
    Page<Place> findPlaceBySearchWord(@Param("searchWord") String searchWord,
                                      Pageable pageable);



    List<Place> findByPlaceNameContaining(String keyword, Pageable pageable);


    /**
     * 암호화된 요양기호로 시설(place)를 검색합니다.
     * @param ykiho 암호화된 요양기호
     * @return
     */
    Optional<Place> findPlaceByYkiho(@Param("ykiho")String ykiho);


//    Page<Place> findByNameContaining(String keyword, Pageable pageable);

//    Page<Place> findPlaceByPlaceNameContainsAndPageable(String keyword, Pageable pageable);


    /**
     * 특정 읍면동(dong) 지역 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param dongId
     * @param pageable
     * @return
     */
    @Query("select p from Place p " +
            "Where p.dong.dongId = :dongId")
    Page<Place> findPlacesByDongId(@Param("dongId") Long dongId, Pageable pageable);

    /**
     * 특정 읍면동(dong) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     *
     * @param dongId     특정 동 지역의 ID값
     * @param categoryId 장소의 카테고리 ID값
     * @param pageable   페이지네이션 정보
     * @return 해당 동 지역에 속하고 지정된 카테고리에 해당하는 장소 목록
     */
    @Query("select p from Place p " +
            "JOIN FETCH p.category c " +
            "Where p.dong.dongId = :dongId " +
            "and p.category.categoryId = :categoryId")
    Page<Place> findPlacesByDongIdAndCategoryId(@Param("dongId") Long dongId,
                                                @Param("categoryId") Long categoryId,
                                                Pageable pageable);



    /**
     * 특정 읍면동(dong) 지역 ID와 카테고리 ID, 검색어 기준으로 장소를 조회하는 메서드 입니다.
     * @param dongId 특정 동 지역의 ID값
     * @param categoryId     장소의 카테고리 ID값
     * @param pageable     페이지네이션 정보
     * @param searchWord 유저가 입력한 검색어
     * @param pageable
     * @return 해당 동 지역에 속하고 지정된 카테고리에 해당하는 장소 목록
     */
    @Query("select p from Place p " +
            "JOIN FETCH p.category c " +
            "Where p.dong.dongId = :dongId " +
            "and p.category.categoryId = :categoryId " +
            "and p.placeName LIKE %:searchWord%")
    Page<Place> findPlacesByDongIdAndCategoryIdAndSearchWord(@Param("dongId") Long dongId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("searchWord") String searchWord,
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
     * sigungu id로 해당하는 읍면동(Dong)에 매핑된 Place를 찾는 메서드
     *
     * @param sigunguId
     * @return
     */
    @Query("SELECT p FROM Place p WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu.sigunguId = :sigunguId)")
    Page<Place> findPlacesBySigunguId(@Param("sigunguId") Long sigunguId, Pageable pageable);


    /**
     * 특정 시군구(sigungu) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param sigunguId
     * @param categoryId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu.sigunguId = :sigunguId) and " +
            "p.category.categoryId =:categoryId")
    Page<Place> findPlacesBySigunguIdAndCategoryId(@Param("sigunguId") Long sigunguId,
                                                   @Param("categoryId") Long categoryId,
                                                   Pageable pageable);


    @Query("SELECT p FROM Place p WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu.sigunguId = :sigunguId) and " +
            "p.category.categoryId =:categoryId " +
            "and p.placeName like  %:searchWord%")
    Page<Place> findPlacesBySigunguIdAndCategoryIdAndSearchWord(@Param("sigunguId") Long sigunguId,
                                                                @Param("categoryId") Long categoryId,
                                                                @Param("searchWord") String searchWord,
                                                                Pageable pageable);





    /**
     * @param sidoId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu IN " +
            "(SELECT s FROM Sigungu s Where s.sido.sidoId = :sidoId))")
    Page<Place> findPlacesBySidoId(@Param("sidoId") Long sidoId, Pageable pageable);

    /**
     * 특정 시도(ion) 지역 ID와 카테고리 ID 기준으로 장소를 조회하는 메서드 입니다.
     * @param sidoId
     * @param categoryId
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.category c " +
            "WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu IN " +
            "(SELECT s FROM Sigungu s Where s.sido.sidoId = :sidoId)) and " +
            "p.category.categoryId =:categoryId")
    Page<Place> findPlacesBySidoIdAndCategoryId(@Param("sidoId") Long sidoId,
                                                @Param("categoryId") Long categoryId,
                                                Pageable pageable);





    /**
     * 특정 시도 지역 ID와 카테고리 ID, 검색어 기준으로 장소를 조회하는 메서드 입니다.
     *
     * @param sidoId
     * @param categoryId
     * @param searchWord 유저가 입력한 검색어
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "JOIN FETCH p.category c " +
            "WHERE p.dong IN " +
            "(SELECT d FROM Dong d WHERE d.sigungu IN " +
            "(SELECT s FROM Sigungu s Where s.sido.sidoId = :sidoId)) and " +
            "p.category.categoryId =:categoryId " +
            "and p.placeName LIKE %:searchWord%")
    Page<Place> findPlacesBySidoIdAndCategoryIdAndSearchWord(@Param("sidoId") Long sidoId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("searchWord") String searchWord,
                                                             Pageable pageable);


//    /**
//     *
//     * @param latitude 유저의 위도입니다. 33.11~37.66
//     * @param longitude 유저의 경도입니다. 124.60~131.87
//     * @return
//     */
//    @Query("SELECT p FROM Place p " +
//            "WHERE ST_Distance_Sphere(p.location, POINT(:longitude, :latitude)) < 1000")
//    List<Place> findPlacesByUserLocation(@Param("latitude") double latitude, @Param("longitude") double longitude,Pageable pageable);
    /**
     *
     * @param latitude 유저의 위도입니다. 33.11~37.66
     * @param longitude 유저의 경도입니다. 124.60~131.87
     * @return
     */
    @Query("SELECT p FROM Place p " +
            "WHERE FUNCTION('ST_Distance_Sphere', p.location, FUNCTION('POINT', :longitude, :latitude)) < 10000")
    Page<Place> findPlacesByUserLocation(@Param("latitude") double latitude, @Param("longitude") double longitude,Pageable pageable);


}

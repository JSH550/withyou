package com.js.withyou.repository;

import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PlaceSearchRepository {

    // EntityManager를 Bean으로 주입
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 동적 조건에 따라 장소를 검색합니다.
     *
     * @param searchWord   장소명 (검색 조건)
     * @param categoryId   카테고리명 (검색 조건)
     * @param departmentId 전공명 (검색 조건)
     * @return 검색된 장소 목록
     */
    public List<Place> searchPlaces(String searchWord, Long categoryId, Integer departmentId, String regionType, Long regionId, Pageable pageable) {
        // 기본 쿼리문 작성
        StringBuilder query = new StringBuilder("SELECT p FROM Place p ");
        List<String> conditions = new ArrayList<>();//쿼리 조건문을 문자열 List

        /**
         *  department 값이 있을경우, PlaceDepartment와 JOIN 연산 조건문을 추가합니다.
         *  PlaceDepartment는 place와 department를 연결하는 테이블입니다.
         */
        if (departmentId != null) {
            query.append("JOIN PlaceDepartment d ON p.placeId = d.place.placeId ");
            conditions.add("d.department.departmentId  = :departmentId ");
        }

        if (regionType != null && !regionType.isEmpty()) {
            switch (regionType) {
                case "dong" -> {
                    conditions.add("p.dong.dongId = :regionId");
                }
                case "sigungu" -> {
                    conditions.add("p.dong IN (select d from Dong d where d.sigungu.sigunguId = :regionId)");
                }
                case "sido" -> {
                    conditions.add("p.dong IN (select d from Dong d " +
                            "where d.sigungu " +
                            "IN(select s from Sigungu s where sigungu.sido.sidoId = :regionId))");

                }
                default -> {
                }
            }

        }

        // 검색어(search word)가있을때 조건문을 추가합니다.
        if (searchWord != null && !searchWord.isEmpty()) {
            conditions.add("p.placeName LIKE :searchWord");
        }
        // 카테고리(categoryId) 정보가 있을 때 조건문을 추가합니다.
        if (categoryId != null) {
            conditions.add("p.category.categoryId = :categoryId");
        }

        // 조건문이 비어있지 않으면 조건문들을 연결합니다.
        if (!conditions.isEmpty()) {
            // StringBuilder append 메서드로 쿼리문 뒤에 WHERE 문자열을 추가합니다.
            // String join 메서드로 리스트 안의 조건들을 " AND "로 연결합니다.
            query.append("WHERE ").append(String.join(" AND ", conditions));
        }






        /**
         * TypedQuery 객체를 생성합니다.
         * createQuery(query.toString() 파라미터로 받은 query 객체를 문자열로 반환하여 JPQL 쿼리를 만듭니다.
         * Place.class는 쿼리가 반환할 타입입니다.
         */
        TypedQuery<Place> typedQuery = entityManager.createQuery(query.toString(), Place.class);
        // 쿼리에 바인딩할 파라미터를 설정합니다.
        // setParameter("쿼리에서 :이름 으로 명명된 부분", "집어넣을 값")
        if (searchWord != null) {
            typedQuery.setParameter("searchWord", "%" + searchWord + "%");
        }
        if (regionType != null && !regionType.isEmpty()) {
            typedQuery.setParameter("regionId", regionId);
        }
        if (categoryId != null) {
            typedQuery.setParameter("categoryId", categoryId);
        }
        if (departmentId != null) {
            typedQuery.setParameter("departmentId", departmentId);
        }





        // 결과 리스트와 전체 레코드 수 가져오기
        List<Place> places = typedQuery.getResultList();




        // 쿼리 실행 후 결과 리스트를 반환합니다.
        return typedQuery.getResultList();
    }
}

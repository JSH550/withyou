package com.js.withyou.repository;

import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j

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
    public Page<Place> searchPlaces(String searchWord, Long categoryId, Integer departmentId, String regionType, Long regionId, Pageable pageable) {
        // 기본 쿼리문 작성
        StringBuilder query = new StringBuilder("SELECT p FROM Place p JOIN FETCH p.category c ");
        StringBuilder selectQuery = new StringBuilder("SELECT COUNT(p) FROM Place p JOIN p.category c ");
        List<String> conditions = new ArrayList<>();//쿼리 조건문을 문자열 List

        /**
         *  department 값이 있을경우, PlaceDepartment와 JOIN 연산 조건문을 추가합니다.
         *  PlaceDepartment는 place와 department를 연결하는 테이블입니다.
         */
        if (departmentId != null) {
            query.append("JOIN PlaceDepartment pd ON p.placeId = pd.place.placeId ");
            selectQuery.append("JOIN PlaceDepartment pd ON p.placeId = pd.place.placeId ");

            //유저가 치과를 선택했을 경우, 치과와 관련된 진료과목 포함하는 place 반환합니다.
            if (departmentId == 49) {
                conditions.add("pd.department.departmentId BETWEEN :minDepartmentId AND :maxDepartmentId ");
//                conditions.add("pd.department.departmentId = 49 ");
//                conditions.add("pd.department.departmentId BETWEEN 50 and 61 ");


            } else {
                conditions.add("pd.department.departmentId  = :departmentId ");


            }
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
            if (categoryId == 71L) {
                conditions.add("c.categoryId BETWEEN :minCategoryId AND :maxCategoryId ");

            } else
                conditions.add("c.categoryId = :categoryId");
            {
            }
        }

        // 조건문이 비어있지 않으면 조건문들을 연결합니다.
        if (!conditions.isEmpty()) {
            // StringBuilder append 메서드로 쿼리문 뒤에 WHERE 문자열을 추가합니다.
            // String join 메서드로 리스트 안의 조건들을 " AND "로 연결합니다.
            query.append("WHERE ").append(String.join(" AND ", conditions));
            selectQuery.append("WHERE ").append(String.join(" AND ", conditions));
        }


        // 전체 레코드 수를 구하는 쿼리
        TypedQuery<Long> countTypedQuery = entityManager.createQuery(selectQuery.toString(), Long.class);
        /**
         * TypedQuery 객체를 생성합니다.
         * createQuery(query.toString() 파라미터로 받은 query 객체를 문자열로 반환하여 JPQL 쿼리를 만듭니다.
         * Place.class는 쿼리가 반환할 타입입니다.
         */
        TypedQuery<Place> typedQuery = entityManager.createQuery(query.toString(), Place.class);
        // 쿼리에 바인딩할 파라미터를 설정합니다.
        // setParameter("쿼리에서 :이름 으로 명명된 부분", "집어넣을 값")
        if (searchWord != null && !searchWord.isEmpty()) {
            typedQuery.setParameter("searchWord", "%" + searchWord + "%");
            countTypedQuery.setParameter("searchWord", "%" + searchWord + "%");
        }
        if (regionType != null && !regionType.isEmpty()) {
            typedQuery.setParameter("regionId", regionId);
            countTypedQuery.setParameter("regionId", regionId);
        }
        if (categoryId != null) {
            if (categoryId == 71L) {
                typedQuery.setParameter("minCategoryId", 71);
                typedQuery.setParameter("maxCategoryId", 75);

                countTypedQuery.setParameter("minCategoryId", 71);
                countTypedQuery.setParameter("maxCategoryId", 75);

            }

            else {
                typedQuery.setParameter("categoryId", categoryId);
                countTypedQuery.setParameter("categoryId", categoryId);

            }

        }
        if (departmentId != null) {

            if (departmentId == 49) {
                typedQuery.setParameter("minDepartmentId", 49);
                typedQuery.setParameter("maxDepartmentId", 61);
//
                countTypedQuery.setParameter("minDepartmentId", 49);
                countTypedQuery.setParameter("maxDepartmentId", 61);
            } else {
                typedQuery.setParameter("departmentId", departmentId);
                countTypedQuery.setParameter("departmentId", departmentId);

            }
        }


        log.info("PlaceSearch 쿼리 = {}", countTypedQuery);

        // 페이징 적용
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // 결과 리스트와 전체 레코드 수 가져오기
        List<Place> places = typedQuery.getResultList();
        Long total = countTypedQuery.getSingleResult();


        // 쿼리 실행 후 결과 리스트를 반환합니다.
//        return typedQuery.getResultList();

        return new PageImpl<>(places, pageable, total);

    }
}

package com.js.withyou.repository.Place;

import com.js.withyou.data.entity.Place.Place;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class PlaceSearchNativeRepository {
    // EntityManager를 Bean으로 주입
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * @param searchWord
     * @param categoryId
     * @param departmentId
     * @param regionType
     * @param regionId
     * @param pageable
     * @return
     */
    public List<Place> searchPlaceByNativeQuery(String searchWord,
                                                Long categoryId,
                                                Integer departmentId,
                                                String regionType,
                                                Long regionId,
                                                Pageable pageable) {


        /**
         * 기본 쿼리문입니다.
         * 컬럼 충돌을 피하기 위해 select문에 alias로 입력하였습니다.
         * 중복 쿼리 제거를 위해 DISTINCT 추가하였습니다.(MySQL 기준 작성)
         */
        StringBuilder query = new StringBuilder("SELECT DISTINCT p.* FROM Place p ");

        //페이지네이션을 위한 기본 쿼리문입니다.
//        StringBuilder countQuery = new StringBuilder("SELECT COUNT(*) FROM Place p ");

        List<String> conditions = new ArrayList<>();//쿼리 조건문을 문자열 List

        if (departmentId != null) {
            query.append("join place_department pd ON p.place_id = pd.place_id ");
//            countQuery.append("join place_department pd ON p.place_id = pd.place_id ");

            //유저가 치과를 선택했을 경우, 치과와 관련된 진료과목 포함하는 place 반환합니다.
            if (departmentId == 49) {
                conditions.add("pd.department_Id BETWEEN :minDepartmentId AND :maxDepartmentId ");
            } else {
                conditions.add("pd.department_Id  = :departmentId ");
            }
        }


        // 검색어(search word)가있을때 조건문을 추가합니다.
        if (searchWord != null && !searchWord.isEmpty()) {
            conditions.add("MATCH(p.place_name) AGAINST(:searchWord IN BOOLEAN MODE)");

        }

        // 조건이 있을경우, 쿼리문에 WHERE절을 추가하고 조건을 AND로 묶습니다.
        if (!conditions.isEmpty()) {
            query.append("WHERE ").append(String.join(" AND ", conditions));
        }

        // EntityManager를 사용하여 쿼리문 객체 선언
        Query nativeQuery = entityManager.createNativeQuery(query.toString(), Place.class);

//        Query countNativeQuery = entityManager.createNativeQuery(query.toString(),Place.class);


        //department 파라미터 바인딩
        if (departmentId != null) {

            if (departmentId == 49) {
                nativeQuery.setParameter("minDepartmentId", 49);
                nativeQuery.setParameter("maxDepartmentId", 61);
//                countNativeQuery.setParameter("minDepartmentId", 49);
//                countNativeQuery.setParameter("maxDepartmentId", 61);
            } else {
                nativeQuery.setParameter("departmentId", departmentId);
//                countNativeQuery.setParameter("departmentId", departmentId);
            }
        }


        // 검색어(search word)가 있을 때 파라미터를 바인딩합니다.

        if (searchWord != null && !searchWord.isEmpty()) {
            nativeQuery.setParameter("searchWord", searchWord);//searchWord 바인딩
//            countNativeQuery.setParameter("searchWord", searchWord);//searchWord 바인딩
        }

        // 페이지네이션 설정
        nativeQuery.setFirstResult((int) pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        //결과 Place List 가져오기
        List<Place> places = nativeQuery.getResultList();

        return places;


        // 전체 레코드 결과 수 가져오기
//        List resultList = countNativeQuery.getResultList();


        //페이지 객체로 변환하여 반환
//        return new PageImpl<>(places, pageable, total);

    }


    public List<Place> searchPlaceBySearchWordAndNativeQuery(String searchWord) {


        //기본 쿼리문입니다.
        StringBuilder query = new StringBuilder("SELECT * FROM Place p ");

        List<String> conditions = new ArrayList<>();//쿼리 조건문을 문자열 List


        // 검색어(search word)가있을때 조건문을 추가합니다.
        if (searchWord != null && !searchWord.isEmpty()) {
            conditions.add("MATCH(p.place_name) AGAINST(:searchWord IN BOOLEAN MODE)");

        }

        if (!conditions.isEmpty()) {
            query.append("WHERE ").append(String.join(" AND ", conditions));
        }

        // EntityManager를 사용하여 쿼리문 형성
        Query nativeQuery = entityManager.createNativeQuery(query.toString(), Place.class);


        // 검색어(search word)가 있을 때 파라미터를 바인딩합니다.

        if (searchWord != null && !searchWord.isEmpty()) {
            nativeQuery.setParameter("searchWord", searchWord);//searchWord 바인딩
        }

//        페이지네이션 안할때 리턴값입니다.
        return nativeQuery.getResultList();

    }


}

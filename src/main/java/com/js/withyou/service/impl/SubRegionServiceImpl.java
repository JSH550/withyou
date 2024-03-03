package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Region;
import com.js.withyou.data.entity.SubRegion;
import com.js.withyou.repository.RegionRepository;
import com.js.withyou.repository.SubRegionRepository;
import com.js.withyou.service.RegionService;
import com.js.withyou.service.SubRegionService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
@Slf4j
@Service
public class SubRegionServiceImpl implements SubRegionService {

   private final SubRegionRepository subRegionRepository;
    private final RegionRepository regionRepository;
    private final RegionService regionService;
    public SubRegionServiceImpl(SubRegionRepository subRegionRepository, RegionRepository regionRepository, RegionService regionService) {
        this.subRegionRepository = subRegionRepository;
        this.regionRepository = regionRepository;
        this.regionService = regionService;
    }


    //시군구 정보 중복으로 인해 시도 정보를 이용해 시군구 정보 조회(예: 부산시 북구, 인천시 북구)
    @Transactional
    @Override
//    public SubRegionDto getSubRegionByRegionNameAndSubRegionName(String regionName,String subRegionName) {
        public SubRegion getSubRegionByRegionNameAndSubRegionName(String regionName, String subRegionName) {

        //subRegionName을 DB와 mapping하기위한 메서드 초기화
        initializeSubRegionMap();
        //DB와 매칭되지 않는 subRegionName은 convert해서 매칭되는 이름으로 재할당, 매칭되는 이름일경우 재할당하지 않음
        String cityName = subRegionName.substring(0, 2);
        log.info("cityName={}",cityName);
        if (Pattern.matches("부산|인천|대구|광주|대전|울산|부천|성남|수원|안산|안양|고양|용인|청주|천안|전주|포항|창원",cityName)){
            //subRegionName DB에 맞게 재할당
            subRegionName= convertSubRegionMap.get(subRegionName);
        }
        log.info("Input regionName={}",regionName);


//        //DB에서 시도 이름으로 entity 검색후 반환
        Optional<Region> foundRegion = regionRepository.findByRegionShortName(regionName);
        log.info("Returned regionName={}",foundRegion.get().getRegionName());
        //반환된 Data 유효성 검사
        if (foundRegion.isPresent()) {
            Region region = foundRegion.get();//조회된 값이 있으면 객체로 꺼냄
            final String finalSubRegionName = subRegionName;
            Optional<SubRegion> foundSubRegion = region.getSubRegions().stream()
                    .filter(subRegion -> subRegion.getSubRegionName().equals(finalSubRegionName))
                    .findFirst();
            log.info("Returned subRegionName={}",foundSubRegion.get().getSubRegionName());
//            return convertToSubRegionDto(foundSubRegion.get());
            return foundSubRegion.get();
        }else {
            throw new IllegalArgumentException("조회된 값이 없습니다.");
        }

    }

    /**
     * 검색 키워드를 기반으로 지역의 시군구(SubRegion) ID 리스트를 조회하는 메서드
     * @param searchKeyword 검색 키워드
     * @return 시군구 ID 리스트, 검색 키워드 or 시도(Region)검색정보가 없을경우 빈 리스트 반환
     */
    @Override
    public List<Long> findSubRegionIdsByKeyword(String searchKeyword){
        if (searchKeyword==null){//키워드 파라미터가 null이면 빈 리스트 반환
            return Collections.emptyList();
        }
        List<RegionDto> foundRegionList = regionService.getRegionByKeyword(searchKeyword);//keyword로 시도 데이터 검색
        if (foundRegionList.isEmpty()){//검색된 시도 데이터가 없으면 빈 리스트 반환
            return Collections.emptyList();
        }
        List<Long> subRegionIds = new ArrayList<>();//시군구 entity의 ID를 저장하기 위한 List 추가
        for (RegionDto regionDto : foundRegionList) {
            List<SubRegion> subRegions = regionDto.getSubRegions();
            for (SubRegion subRegion : subRegions) {
                subRegionIds.add(subRegion.getSubRegionId());//반복문으로 모든 시군구 entity의 ID값을 List에 저장
            }
        }
        return subRegionIds;
    }


    public SubRegionDto convertToSubRegionDto(SubRegion subRegion){
        SubRegionDto subRegionDto = new SubRegionDto();
        subRegionDto.setSubRegionId(subRegion.getSubRegionId());
        subRegionDto.setSubRegionName(subRegion.getSubRegionName());
        subRegionDto.setRegionName(subRegion.getRegion().getRegionName());

        return subRegionDto;
    }

    //건강보험 XML Data에서 DB 데이터와 맞지 않는 부분 convert 하기위한 HashMap
    private Map<String, String> convertSubRegionMap;
    public void initializeSubRegionMap() {
        Map<String, String> subRegionMap = new HashMap<>();
        //부산
        subRegionMap.put("부산남구", "남구");
        subRegionMap.put("부산동구", "동구");
        subRegionMap.put("부산동래구", "동래구");
        subRegionMap.put("부산북구", "북구");
        subRegionMap.put("부산서구", "서구");
        subRegionMap.put("부산영도구", "영도구");
        subRegionMap.put("부산중구", "중구");
        subRegionMap.put("부산해운대구", "해운대구");
        subRegionMap.put("부산사하구", "사하구");
        subRegionMap.put("부산금정구", "금정구");
        subRegionMap.put("부산강서구", "강서구");
        subRegionMap.put("부산연제구", "연제구");
        subRegionMap.put("부산수영구", "수영구");
        subRegionMap.put("부산사상구", "사상구");
        subRegionMap.put("부산기장군", "기장군");
        subRegionMap.put("부산진구", "부산진구");

        //인천시
        subRegionMap.put("인천미추홀구", "미추홀구");
        subRegionMap.put("인천동구", "동구");
        subRegionMap.put("인천부평구", "부평구");
        subRegionMap.put("인천중구", "중구");
        subRegionMap.put("인천서구", "서구");
        subRegionMap.put("인천남동구", "남동구");
        subRegionMap.put("인천연수구", "연수구");
        subRegionMap.put("인천계양구", "계양구");
        subRegionMap.put("인천강화군", "강화군");
        subRegionMap.put("인천옹진군", "옹진군");
        //대구광역시
        subRegionMap.put("대구남구", "남구");
        subRegionMap.put("대구동구", "동구");
        subRegionMap.put("대구북구", "북구");
        subRegionMap.put("대구서구", "서구");
        subRegionMap.put("대구수성구", "수성구");
        subRegionMap.put("대구중구", "중구");
        subRegionMap.put("대구달서구", "달서구");
        subRegionMap.put("대구달성군", "달성군");
        subRegionMap.put("대구군위군", "군위군");
        //광주광역시
        subRegionMap.put("광주동구", "동구");
        subRegionMap.put("광주북구", "북구");
        subRegionMap.put("광주서구", "서구");
        subRegionMap.put("광주광산구", "광산구");
        subRegionMap.put("광주남구", "남구");

        //광주시
        subRegionMap.put("광주시", "광주시");

        //대전광역시
        subRegionMap.put("대전유성구", "유성구");
        subRegionMap.put("대전대덕구", "대덕구");
        subRegionMap.put("대전서구", "서구");
        subRegionMap.put("대전동구", "동구");
        subRegionMap.put("대전중구", "중구");
        //울산광역시
        subRegionMap.put("울산남구", "남구");
        subRegionMap.put("울산동구", "동구");
        subRegionMap.put("울산중구", "중구");
        subRegionMap.put("울산북구", "북구");
        subRegionMap.put("울산울주군", "울주군");
        //부천시
        subRegionMap.put("부천소사구", "부천시");
        subRegionMap.put("부천오정구", "부천시");
        subRegionMap.put("부천원미구", "부천시");
        //성남시
        subRegionMap.put("성남수정구", "성남시 수정구");
        subRegionMap.put("성남중원구", "성남시 중원구");
        subRegionMap.put("성남분당구", "성남시 분당구");

        //수원시
        subRegionMap.put("수원권선구", "수원시 권선구");
        subRegionMap.put("수원장안구", "수원시 장안구");
        subRegionMap.put("수원팔달구", "수원시 팔달구");
        subRegionMap.put("수원영통구", "수원시 영통구");
        //안양시
        subRegionMap.put("안양만안구", "안양시 만안구");
        subRegionMap.put("안양동안구", "안양시 동안구");
        subRegionMap.put("안산단원구", "안산시 단원구");
        subRegionMap.put("안산상록구", "안산시 상록구");
        //고양시
        subRegionMap.put("고양덕양구", "고양시 덕양구");
        subRegionMap.put("고양일산서구", "고양시 일산서구");
        subRegionMap.put("고양일산동구", "고양시 일산동구");
        //용인시
        subRegionMap.put("용인기흥구", "용인시 기흥구");
        subRegionMap.put("용인수지구", "용인시 수지구");
        subRegionMap.put("용인처인구", "용인시 처인구");
        //청주시
        subRegionMap.put("청주상당구", "청주시 상당구");
        subRegionMap.put("청주흥덕구", "청주시 흥덕구");
        subRegionMap.put("청주청원구", "청주시 청원구");
        subRegionMap.put("청주서원구", "청주시 서원구");
        //천안시
        subRegionMap.put("천안서북구", "천안시 서북구");
        subRegionMap.put("천안동남구", "천안시 동남구");
        //전주시
        subRegionMap.put("전주완산구", "전주시 완산구");
        subRegionMap.put("전주덕진구", "전주시 덕진구");
        //포항시
        subRegionMap.put("포항남구", "포항시 남구");
        subRegionMap.put("포항북구", "포항시 북구");
        //창원시
        subRegionMap.put("창원마산회원구", "창원시 마산회원구");
        subRegionMap.put("창원마산합포구", "창원시 마산합포구");
        subRegionMap.put("창원진해구", "창원시 진해구");
        subRegionMap.put("창원의창구", "창원시 의창구");
        subRegionMap.put("창원성산구", "창원시 성산구");
        //read only상태로 저장
        convertSubRegionMap  = Collections.unmodifiableMap(subRegionMap);

    };


}

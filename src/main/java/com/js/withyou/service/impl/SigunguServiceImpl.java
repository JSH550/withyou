package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.data.dto.Sigungu.SigunguNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguSearchDto;
import com.js.withyou.data.entity.Region.Sido;
import com.js.withyou.data.entity.Region.Sigungu;
import com.js.withyou.repository.SidoRepository;
import com.js.withyou.repository.SigunguRepository;
import com.js.withyou.service.SidoService;
import com.js.withyou.service.SigunguService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SigunguServiceImpl implements SigunguService {

    private final SigunguRepository sigunguRepository;
    private final SidoRepository sidoRepository;
    private final SidoService sidoService;
    //건강보험 XML Data에서 DB 데이터와 맞지 않는 부분 convert 하기위한 HashMap
    private Map<String, String> convertSubRegionMap;


    public SigunguServiceImpl(SigunguRepository sigunguRepository, SidoRepository sidoRepository, SidoService sidoService) {
        this.sigunguRepository = sigunguRepository;
        this.sidoRepository = sidoRepository;
        this.sidoService = sidoService;
    }

    //시군구 정보 중복으로 인해 시도 정보를 이용해 시군구 정보 조회(예: 부산시 북구, 인천시 북구)
    @Transactional
    @Override
    public Sigungu getSigunguBySidoNameAndSigunguName(String sidoName, String sigunguName) {

        //sigunguName을 DB와 mapping하기위한 메서드 초기화
        initializeSigunguMap();
        //DB와 매칭되지 않는 sigunguName은 convert해서 매칭되는 이름으로 재할당, 매칭되는 이름일경우 재할당하지 않음
        String cityName = sigunguName.substring(0, 2);
        log.info("cityName={}", cityName);
        if (Pattern.matches("부산|인천|대구|광주|대전|울산|부천|성남|수원|안산|안양|고양|용인|청주|천안|전주|포항|창원|세종", cityName)) {
            //sigunguName DB에 맞게 재할당
            sigunguName = convertSubRegionMap.get(sigunguName);
        }
        if (sidoName.equals("세종시")) {
            sidoName = "세종";
        }

        log.info("Input sidoName={}", sidoName);


//        //DB에서 시도 이름으로 entity 검색후 반환
        Optional<Sido> foundSido = sidoRepository.findSidoBySidoShortName(sidoName);
        log.info("Returned sidoName={}", foundSido.get().getSidoName());
        //반환된 Data 유효성 검사
        if (foundSido.isPresent()) {
            Sido sido = foundSido.get();//조회된 값이 있으면 객체로 꺼냄
            final String finalSigunguName = sigunguName;
            Optional<Sigungu> foundSigungu = sido.getSigungus().stream()
                    .filter(sigungu -> sigungu.getSigunguName().equals(finalSigunguName))
                    .findFirst();
            log.info("Returned sigunguName={}", foundSigungu.get().getSigunguName());
            return foundSigungu.get();
        } else {
            throw new IllegalArgumentException("조회된 값이 없습니다.");
        }

    }

    /**
     * 검색 키워드를 기반으로 지역의 시군구(Sigungu) ID 리스트를 조회하는 메서드
     *
     * @param searchKeyword 검색 키워드
     * @return 시군구 ID 리스트, 검색 키워드 or 시도(Sido)검색정보가 없을경우 빈 리스트 반환
     */
    @Override
    public List<Long> findSigunguIdsByKeyword(String searchKeyword) {
        if (searchKeyword == null) {//키워드 파라미터가 null이면 빈 리스트 반환
            return Collections.emptyList();
        }
        List<SidoDto> foundSidoDtoList = sidoService.getSidoDtoListByKeyword(searchKeyword);//keyword로 시도 데이터 검색
        if (foundSidoDtoList.isEmpty()) {//검색된 시도 데이터가 없으면 빈 리스트 반환
            return Collections.emptyList();
        }
        List<Long> sigunguIds = new ArrayList<>();//시군구 entity의 ID를 저장하기 위한 List 추가
        for (SidoDto sidoDto : foundSidoDtoList) {
            List<SigunguDto> sigunguDtoList = sidoDto.getSigungus();
            for (SigunguDto sigungu : sigunguDtoList) {
                sigunguIds.add(sigungu.getSigunguId());//반복문으로 모든 시군구 entity의 ID값을 List에 저장
            }
        }
        return sigunguIds;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SigunguSearchDto> getSigunguSearchDtosByKeyword(String keyword) {
        List<Sigungu> sigunguList = sigunguRepository.findBySigunguNameContaining(keyword);
        if (sigunguList.isEmpty()) {
            List<SigunguSearchDto> sigunguSearchDtos = new ArrayList<>();//검색 결과가 없을경우 빈 List를 반환합니다.
            return sigunguSearchDtos;

        }

        List<SigunguSearchDto> sigunguSearchDtos = sigunguList.stream().map(Sigungu -> SigunguSearchDto.builder()
                .sigunguId(Sigungu.getSigunguId())
                .sigunguNameLong(Sigungu.getSido().getSidoName() + " " + Sigungu.getSigunguName())
                .build()).collect(Collectors.toList());

        return sigunguSearchDtos;
    }

    @Override
    public List<SigunguNameDto> getSigunguNameDtoBySidoId(Long sidoId) {

        List<Sigungu> sigunguList = sigunguRepository.findSigunguBySidoSidoId(sidoId);

        List<SigunguNameDto> sigunguNameDtoList = sigunguList.stream()
                .map(Sigungu -> new SigunguNameDto().convertToSigunguNameDto(Sigungu))
                .collect(Collectors.toList());

        return sigunguNameDtoList;
//        sigunguRepository.find


    }

    @Override
    public SigunguNameDto getSigunguNameDtoBySidoIdAndSigunguName(Long sidoId, String sigunguName) {

        Optional<Sigungu> foundSigungu = sigunguRepository.findBySidoIdAndSigunguName(sidoId, sigunguName);
        SigunguNameDto sigunguNameDto = new SigunguNameDto();

        if (foundSigungu.isPresent()) {
            log.info("찾은 시군구 ={} ",foundSigungu.get().getSigunguName());
          return sigunguNameDto.convertToSigunguNameDto(foundSigungu.get());


        }

    return  sigunguNameDto;




    }
    public void initializeSigunguMap() {
        Map<String, String> SigunguMap = new HashMap<>();
        //부산
        SigunguMap.put("부산남구", "남구");
        SigunguMap.put("부산동구", "동구");
        SigunguMap.put("부산동래구", "동래구");
        SigunguMap.put("부산북구", "북구");
        SigunguMap.put("부산서구", "서구");
        SigunguMap.put("부산영도구", "영도구");
        SigunguMap.put("부산중구", "중구");
        SigunguMap.put("부산해운대구", "해운대구");
        SigunguMap.put("부산사하구", "사하구");
        SigunguMap.put("부산금정구", "금정구");
        SigunguMap.put("부산강서구", "강서구");
        SigunguMap.put("부산연제구", "연제구");
        SigunguMap.put("부산수영구", "수영구");
        SigunguMap.put("부산사상구", "사상구");
        SigunguMap.put("부산기장군", "기장군");
        SigunguMap.put("부산진구", "부산진구");

        //인천시
        SigunguMap.put("인천미추홀구", "미추홀구");
        SigunguMap.put("인천동구", "동구");
        SigunguMap.put("인천부평구", "부평구");
        SigunguMap.put("인천중구", "중구");
        SigunguMap.put("인천서구", "서구");
        SigunguMap.put("인천남동구", "남동구");
        SigunguMap.put("인천연수구", "연수구");
        SigunguMap.put("인천계양구", "계양구");
        SigunguMap.put("인천강화군", "강화군");
        SigunguMap.put("인천옹진군", "옹진군");
        //대구광역시
        SigunguMap.put("대구남구", "남구");
        SigunguMap.put("대구동구", "동구");
        SigunguMap.put("대구북구", "북구");
        SigunguMap.put("대구서구", "서구");
        SigunguMap.put("대구수성구", "수성구");
        SigunguMap.put("대구중구", "중구");
        SigunguMap.put("대구달서구", "달서구");
        SigunguMap.put("대구달성군", "달성군");
        SigunguMap.put("대구군위군", "군위군");
        //광주광역시
        SigunguMap.put("광주동구", "동구");
        SigunguMap.put("광주북구", "북구");
        SigunguMap.put("광주서구", "서구");
        SigunguMap.put("광주광산구", "광산구");
        SigunguMap.put("광주남구", "남구");

        //광주시
        SigunguMap.put("광주시", "광주시");

        //대전광역시
        SigunguMap.put("대전유성구", "유성구");
        SigunguMap.put("대전대덕구", "대덕구");
        SigunguMap.put("대전서구", "서구");
        SigunguMap.put("대전동구", "동구");
        SigunguMap.put("대전중구", "중구");
        //울산광역시
        SigunguMap.put("울산남구", "남구");
        SigunguMap.put("울산동구", "동구");
        SigunguMap.put("울산중구", "중구");
        SigunguMap.put("울산북구", "북구");
        SigunguMap.put("울산울주군", "울주군");
        //부천시
        SigunguMap.put("부천소사구", "부천시");
        SigunguMap.put("부천오정구", "부천시");
        SigunguMap.put("부천원미구", "부천시");
        //성남시
        SigunguMap.put("성남수정구", "성남시 수정구");
        SigunguMap.put("성남중원구", "성남시 중원구");
        SigunguMap.put("성남분당구", "성남시 분당구");

        //수원시
        SigunguMap.put("수원권선구", "수원시 권선구");
        SigunguMap.put("수원장안구", "수원시 장안구");
        SigunguMap.put("수원팔달구", "수원시 팔달구");
        SigunguMap.put("수원영통구", "수원시 영통구");
        //안양시
        SigunguMap.put("안양만안구", "안양시 만안구");
        SigunguMap.put("안양동안구", "안양시 동안구");
        SigunguMap.put("안산단원구", "안산시 단원구");
        SigunguMap.put("안산상록구", "안산시 상록구");
        //고양시
        SigunguMap.put("고양덕양구", "고양시 덕양구");
        SigunguMap.put("고양일산서구", "고양시 일산서구");
        SigunguMap.put("고양일산동구", "고양시 일산동구");
        //용인시
        SigunguMap.put("용인기흥구", "용인시 기흥구");
        SigunguMap.put("용인수지구", "용인시 수지구");
        SigunguMap.put("용인처인구", "용인시 처인구");
        //청주시
        SigunguMap.put("청주상당구", "청주시 상당구");
        SigunguMap.put("청주흥덕구", "청주시 흥덕구");
        SigunguMap.put("청주청원구", "청주시 청원구");
        SigunguMap.put("청주서원구", "청주시 서원구");
        //천안시
        SigunguMap.put("천안서북구", "천안시 서북구");
        SigunguMap.put("천안동남구", "천안시 동남구");
        //전주시
        SigunguMap.put("전주완산구", "전주시 완산구");
        SigunguMap.put("전주덕진구", "전주시 덕진구");
        //포항시
        SigunguMap.put("포항남구", "포항시 남구");
        SigunguMap.put("포항북구", "포항시 북구");
        //창원시
        SigunguMap.put("창원마산회원구", "창원시 마산회원구");
        SigunguMap.put("창원마산합포구", "창원시 마산합포구");
        SigunguMap.put("창원진해구", "창원시 진해구");
        SigunguMap.put("창원의창구", "창원시 의창구");
        SigunguMap.put("창원성산구", "창원시 성산구");

        //세종시
        SigunguMap.put("세종시", "세종시");

        //read only상태로 저장
        convertSubRegionMap = Collections.unmodifiableMap(SigunguMap);

    }

    ;


}

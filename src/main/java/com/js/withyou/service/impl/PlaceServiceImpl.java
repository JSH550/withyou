package com.js.withyou.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.withyou.data.KeywordDto;
import com.js.withyou.data.dto.CategoryDto;
import com.js.withyou.data.dto.Region.RegionNameDto;
import com.js.withyou.data.dto.SearchSuggestionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionSearchDto;
import com.js.withyou.data.dto.place.PlaceCreateDto;
import com.js.withyou.data.dto.place.PlaceDetailDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.dto.place.PlaceListDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.Place.Place;
import com.js.withyou.data.entity.SubRegion;
import com.js.withyou.repository.CategoryRepository;
import com.js.withyou.repository.PlaceRepository;
import com.js.withyou.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaceServiceImpl implements PlaceService {
    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json?query={address}";
    private final RegionService regionService;
    private final SubRegionService subRegionService;


    private final PlaceRepository placeRepository;

    private final CategoryService categoryService;


    private final CategoryRepository categoryRepository;
    // 수정된 부분: 인스턴스 변수로 변경하여 @Value 어노테이션이 제대로 동작하도록 함
//    @Value("${kakao.restapi.key}")
    private final String KAKAO_API_KEY = "8f51e140457787ad945ffce8f4b6ac5a";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public PlaceServiceImpl(RegionService regionService, SubRegionService subRegionService, PlaceRepository placeRepository, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.regionService = regionService;
        this.subRegionService = subRegionService;
        this.placeRepository = placeRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private static PlaceCreateDto createPlaceSaveDto(String placeName, String roadAddress, String latitude, String longitude) {
        PlaceCreateDto placeCreateDto = new PlaceCreateDto();
        placeCreateDto.setPlaceName(placeName);
        placeCreateDto.setPlaceRoadAddress(roadAddress);
        log.info("도로명결과={}", placeCreateDto.getPlaceRoadAddress());
        placeCreateDto.setPlaceLatitude(Double.parseDouble(latitude));
        placeCreateDto.setPlaceLongitude(Double.parseDouble(longitude));
        return placeCreateDto;
    }

    //DB에 place 정보 저장
    @Transactional
    @Override
    public void savePlace(PlaceCreateDto placeCreateDto, Category category, SubRegion subRegion) {
        Place place = new Place();
        log.info("save메서드 정보입력{},{},{}", subRegion.getSubRegionName(), category.getCategoryId(), subRegion.getSubRegionId());
        Place createPlace = place.createPlace(placeCreateDto, category, subRegion);
        //시군구 정보에 동일한 장소명이 있으면 저장하지 않음.
        List<Place> placesOfSubregion = subRegion.getPlaces();
        //중복 검사를 위한 boolean 객체
        boolean exists = false;
        //subRegion의 placeList 중 중복된 장소명이 있으면 true, 없으면 false 반환
        for (Place foundPlace : placesOfSubregion) {
            if (foundPlace.getPlaceName().equals(placeCreateDto.getPlaceName())) {
                exists = true;
                break;
            }
        }
        log.info("의료기관 중복 검사 결과={}", exists);
        if (exists) {
            log.info("해당 시군구에 동일한 기관명이 존재합니다. 기관명을 확인해 주세요");
        } else {
            Place savedPlace = placeRepository.save(createPlace);
        }

    }

    /**
     * 장소 ID를 기반으로 해당하는 장소 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 ID
     * @return 조회된 장소 정보를 담은 DTO 객체
     * @throws IllegalArgumentException 조회된 장소가 없을 경우 발생하는 예외
     */
    @Override
    public PlaceDto getPlaceByPlaceId(Long placeId) {
        PlaceDto placeDto = new PlaceDto();
        Optional<Place> foundPlace = placeRepository.findById(placeId);
        if (foundPlace.isPresent()) {
            Place place = foundPlace.get();
            return placeDto.convertToPlaceDto(place);
        } else {
            throw new IllegalArgumentException("Place ID에 해당하는 장소를 찾을 수 없습니다. placeId={} " + placeId);
        }
    }

    /**
     * @param placeId DB에서 조회하기 위한 PK 값입니다.
     * @return PlaceDetailDto Place의 모든 내용을 담은 DTO를 반환합니다.
     */
    @Transactional
    @Override
    public PlaceDetailDto getPlaceDetailDtoByPlaceId(Long placeId) {
        PlaceDetailDto placeDetailDto = new PlaceDetailDto();
        Optional<Place> foundPlace = placeRepository.findById(placeId);

        if (foundPlace.isPresent()) {
            return placeDetailDto.convertToPlaceDetailDto(foundPlace.get());

        } else {
            throw new IllegalArgumentException("Place ID에 해당하는 장소를 찾을 수 없습니다. placeId={} " + placeId);
        }

    }

    /**
     * 장소 ID를 기반으로 해당하는 장소 정보를 조회합니다.
     *
     * @param placeId 조회할 장소의 ID
     * @return 조회된 장소 정보를 담은 entity 객체입니다. 컨트롤러로 전송하지 않게 주의합니다.(DTO를 반환하는 메서드를 이용해주세요)
     * @throws IllegalArgumentException 조회된 장소가 없을 경우 발생하는 예외
     */
    @Override
    public Place getPlaceEntityByPlaceId(Long placeId) {
        Optional<Place> foundPlace = placeRepository.findById(placeId);
        if (foundPlace.isPresent()) {
            Place place = foundPlace.get();
            return place;
        } else {
            throw new IllegalArgumentException("Place ID에 해당하는 장소를 찾을 수 없습니다. placeId={} " + placeId);
        }
    }

    /**
     * 특정 검색어를 포함한 장소를 조회하고, 해당 장소 정보를 저장하는 메서드입니다.
     * 이 메서드는 지정된 검색어를 포함하는 장소를 조회하기 위해 1회의 쿼리를 실행합니다.
     * 조회된 장소에 연결된 카테고리와 시군구(SubRegion) 정보는 left join fetch를 사용하여 함께 조회됩니다.
     *
     * @param keyword 조회할 장소명에 포함된 키워드
     * @return 특정 검색어를 포함한 장소의 정보를 담은 DTO 목록
     */
    @Override
    public List<PlaceDto> getPlaceByKeyword(String keyword) {
        // 주어진 키워드를 포함하는 장소를 조회
        List<Place> foundPlaceList = placeRepository.findByPlaceNameContaining(keyword);
        // 조회된 장소 정보를 저장할 DTO 목록 생성
        List<PlaceDto> foundPlaceDtoList = new ArrayList<>();
        // 조회된 각 장소에 대해 DTO로 변환하여 목록에 추가
        for (Place place : foundPlaceList) {
            PlaceDto placeDto = new PlaceDto();
            foundPlaceDtoList.add(placeDto.convertToPlaceDto(place));
        }
        return foundPlaceDtoList;
    }

    @Override
    public List<PlaceListDto> getPlaceByKeywordAndPageable(String keyword, Pageable pageable) {
//        Page<Place> foundPlacePage = placeRepository.findPlaceByPlaceNameContainsAndPageable(keyword, pageable);
        List<Place> foundPlace = placeRepository.findByPlaceNameContaining(keyword, pageable);
        //Place를 PlaceListDto로 매핑
        List<PlaceListDto> placeListDtoList = foundPlace.stream()
                .map(Place -> PlaceListDto.builder()
                        .placeId(Place.getPlaceId())
                        .placeName(Place.getPlaceName())
                        .placeRoadAddress(Place.getPlaceRoadAddress())
                        .categoryName(Place.getCategory().getCategoryName())
                        .subRegionName(Place.getSubRegion().getSubRegionName())
                        .build()).collect(Collectors.toList());
        return placeListDtoList;
//        return null;
    }

    /*
     * 검색어를 포함한 카테고리를 조회하고, 해당 카테고리에 속하는 장소를 저장하는 메서드입니다.
     * 이 메서드는 검색된 카테고리를 조회하기 위해 1회의 쿼리를 실행하며, 각 검색된 카테고리에 매핑된 장소를 조회하기 위해 추가적인 n회의 쿼리를 실행합니다.
     */
    @Transactional
    @Override
    public List<PlaceDto> getPlaceByCategory(String keyword) {
        //검색어를 포함한 category의 DTO List, Place는 DTO로 저장되어있음
        List<CategoryDto> foundCategories = categoryService.findCategoriesByKeyWord(keyword);
        return foundCategories.stream()
                .map(CategoryDto::getPlaces)
                .flatMap(List::stream)//getPlaces로 가져온 PlaceDTO List를 stream으로 변환
                .collect(Collectors.toList());//List로 저장하여 반환
    }

    /**
     * 주어진 시군구의 ID(subRegionId)와 mapping 되는 장소를 검색합니다.
     *
     * @param subRegionIdList 시군구의 ID 목록
     * @return 해당 시군구의 장소 목록을 PlaceDto의 List 형태로 반환합니다.
     */
    public List<PlaceDto> getPlaceBySubRegionIdList(List<Long> subRegionIdList) {
        List<PlaceDto> foundPlaceDtoList = new ArrayList<>();
        for (Long subRegionId : subRegionIdList) {
            List<Place> foundPlaceList = placeRepository.findBySubregionId(subRegionId,Pageable.unpaged());//시군구 Id로 Place 검색
            for (Place place : foundPlaceList) {
                foundPlaceDtoList.add(convertPlaceDto(place));
            }
        }
        return foundPlaceDtoList;
    }

    @Override
    public List<PlaceListDto> getPlacesBySearchSuggestion(SearchSuggestionDto searchSuggestionDto) {

        //searchSuggestionDto null체크, null일경우 빈리스트를 반환합니다.
        if (searchSuggestionDto == null) {
            return Collections.emptyList();
        }

        //페이지네이션을 위한 객체입니다. 디폴트 사이즈는 20입니다. 페이지넘버는 0부터 시작합니다.
        PageRequest pageRequest = PageRequest.of(searchSuggestionDto.getPageNumber(), 20);

        // 타입이 시군구(subRegion)일 경우, 해당 시군구에 속한 place를 반환합니다.
        if (searchSuggestionDto.getDataType().equals("subRegion")) {
            // 시군구(subRegion) ID가 요청된 검색과 동일한 시설(Place)을 반환합니다.
            return getPlaceBySubRegionId(searchSuggestionDto.getId(),pageRequest);
            // 타입이 시도(region)일 경우, 해당 시도에 속한 place를 반환합니다.
        } else if (searchSuggestionDto.getDataType().equals("region")) {
            return getPlacesByRegionId(searchSuggestionDto.getId(),pageRequest);
            // 그외 타입일 경우 시설명(placeName)으로 검색하여 반환합니다.
        }else {
            return  getPlaceByKeywordAndPageable(searchSuggestionDto.getName(), pageRequest);
        }
    }

    @Override
    public List<PlaceListDto> getPlaceBySubRegionId(Long subRegionId, Pageable pageable) {
        List<Place> placeList = placeRepository.findBySubregionId(subRegionId, pageable);
        List<PlaceListDto> placeListDtoList = placeList.stream()
                .map(Place -> PlaceListDto.builder()
                        .placeId(Place.getPlaceId())
                        .placeName(Place.getPlaceName())
                        .placeRoadAddress(Place.getPlaceRoadAddress())
                        .categoryName(Place.getCategory().getCategoryName())
                        .subRegionName(Place.getSubRegion().getSubRegionName())
                        .build()).collect(Collectors.toList());

        return placeListDtoList;


    }

    @Override
    public List<PlaceListDto> getPlacesByRegionId(Long regionId, Pageable pageable) {


        List<Place> placeList = placeRepository.findPlacesByRegionId(regionId,pageable);
        List<PlaceListDto> placeListDtoList = placeList.stream()
                .map(Place -> PlaceListDto.builder()
                        .placeId(Place.getPlaceId())
                        .placeName(Place.getPlaceName())
                        .placeRoadAddress(Place.getPlaceRoadAddress())
                        .categoryName(Place.getCategory().getCategoryName())
                        .subRegionName(Place.getSubRegion().getSubRegionName())
                        .build()).collect(Collectors.toList());

        return placeListDtoList;
    }

    /**
     * 주어진 하위 지역(SubRegionDto) 목록에서 장소(PlaceDto)를 검색합니다.
     * 각 하위 지역에는 여러 개의 장소가 연결되어 있을 수 있습니다.
     * @param subRegionDtoList 하위 지역(SubRegionDto) 목록
     * @return 주어진 하위 지역 목록에 포함된 모든 장소(PlaceDto)의 리스트를 반환합니다.
     */
    @Override
    public List<PlaceDto> getPlaceBySubRegions(List<SubRegionDto> subRegionDtoList) {
        List<PlaceDto> placeDtoList = subRegionDtoList.stream()
                .flatMap(subRegionDto -> {
                    return subRegionDto.getPlaces().stream();
                }).collect(Collectors.toList());
        return placeDtoList;
    }

    /**
     * 특정 키워드로 시도를 찾고, 해당 시도에 속한 하위 시군구에 매핑된 장소를 찾는 메서드입니다.
     * @param keyword 검색에 사용될 키워드 클라이언트로부터 받습니다.
     * @return 특정 키워드로 검색된 시도의 하위 시군구에 매핑된 장소 목록 입니다.
     */
    @Override
    public List<PlaceDto> getPlaceByRegionName(String keyword) {
        List<SubRegionDto> subRegionDtoList = regionService.getSubRegionsByRegionNameContainingKeyword(keyword);
        return getPlaceBySubRegions(subRegionDtoList);
    }

    /**
     * 컨트롤러에서 검색어를 넘겨받으면 그 검색어를 바탕으로 시설을 검색합니다.
     * 1.키워드로 카테고리 검색 후 반환값이 null 이 아니면, 해당 카테고리 ID와 매핑된 시설을 저장합니다.
     * 2.키워드로 시설명 검색 후 반환값이 null 이 아니면, 해당 시설들을 저장합니다.
     * 3.키워드로 시도 정보 검색 후 반환값이 null 이 아니면, 해당 시도 하위 시군구 정보로 매핑된 시설을 저장합니다.
     *
     * @param searchKeyword 검색에 사용될 키워드 입니다.
     * @return List<PlaceDto> 저장된 시군구(Place)정보를 PlaceDto List로 반환합니다.
     */
    @Override
    public List<PlaceDto> searchPlacesByKeyWord(String searchKeyword) {
        List<PlaceDto> placeDtoList = new ArrayList<>();//검색 결과 저장을 위한 PlaceDtoList
        // 1. 시설 카테고리로 검색, 쿼리2회(카테고리 조회1 place 조회 1)
        log.info("카테고리로 시설 검색 검색 시작");
        List<PlaceDto> placeDtoListByCategory = getPlaceByCategory(searchKeyword);
        placeDtoList.addAll(placeDtoListByCategory);
        // 2. 시설 이름으로 검색, 쿼리2회(place 조회 1, region 조회 1)
        log.info("시설 이름으로 검색 시작");
        List<PlaceDto> placeDtoListByKeyword = getPlaceByKeyword(searchKeyword);
        placeDtoList.addAll(placeDtoListByKeyword);
        // 3. 시도(region) 으로 검색
        List<PlaceDto> placeDtoListByRegionName = getPlaceByRegionName(searchKeyword);
        placeDtoList.addAll(placeDtoListByRegionName);
        // 4. 시군구(subRegion)으로 검색
        List<SubRegionDto> subRegionDtoList = regionService.findSubregionByKeyword(searchKeyword);
        List<PlaceDto> placeDtoListBySubRegion = getPlaceBySubRegions(subRegionDtoList);
        placeDtoList.addAll(placeDtoListBySubRegion);

        Set<PlaceDto> uniquePlaceDtos = new HashSet<>(placeDtoList);
        placeDtoList.clear();
        placeDtoList.addAll(uniquePlaceDtos);
        return placeDtoList;

    }

    @Override
    public List<SearchSuggestionDto> getSearchSuggestions(KeywordDto keywordDto) {
        //1. 지역(region) 이름으로 검색
        List<SearchSuggestionDto> searchSuggestionDtoList = new ArrayList<>();//클라이언트에게 전달하기 위한 DTO List 입니다.

        log.info("유저가 요청한 키워드 ={}", keywordDto.getKeyword());
        String keyword = keywordDto.getKeyword();
        List<RegionNameDto> regionNameDtoByKeyword = regionService.getRegionNameDtoByKeyword(keyword);
        if (!regionNameDtoByKeyword.isEmpty()) {
            for (RegionNameDto regionNameDto : regionNameDtoByKeyword) {
                SearchSuggestionDto searchSuggestionDto = new SearchSuggestionDto()
                        .createSearchSuggestionDto(regionNameDto.getRegionId(), "region", regionNameDto.getRegionName());
                searchSuggestionDtoList.add(searchSuggestionDto);
            }
        }
        //2. 세부지역(subregion) 이름으로 검색
        List<SubRegionSearchDto> subRegionSearchDtosByKeyword = subRegionService.getSubRegionSearchDtosByKeyword(keyword);
        if (!subRegionSearchDtosByKeyword.isEmpty()) {
            for (SubRegionSearchDto subRegionSearchDto : subRegionSearchDtosByKeyword) {
                SearchSuggestionDto searchSuggestionDto = new SearchSuggestionDto()
                        .createSearchSuggestionDto(subRegionSearchDto.getSubRegionId()
                                , "subRegion"
                                , subRegionSearchDto.getSubRegionNameLong());
                searchSuggestionDtoList.add(searchSuggestionDto);
//                searchSuggestionDtoList.add(SearchSuggestionDto.builder()
//                        .id(subRegionSearchDto.getSubRegionId())
//                        .dataType("subRegion")
//                        .name(subRegionSearchDto.getSubRegionNameLong())
//                        .build());
//            }
            }
        }
            //3. 시설(place) 이름으로 검색
            PageRequest pageRequest = PageRequest.of(0, 20);
            List<PlaceListDto> placeListDtoList = getPlaceByKeywordAndPageable(keyword,pageRequest);
            if (!placeListDtoList.isEmpty()) {
                for (PlaceListDto placeListDto : placeListDtoList) {
                    SearchSuggestionDto searchSuggestionDto = new SearchSuggestionDto()
                            .createSearchSuggestionDto(placeListDto.getPlaceId()
                                    , "place"
                                    , placeListDto.getPlaceName());
                    searchSuggestionDtoList.add(searchSuggestionDto);

                    log.info("place명={}",searchSuggestionDto.getName());
//                searchSuggestionDtoList.add(SearchSuggestionDto.builder()
//                        .id(placeListDto.getPlaceId())
//                        .dataType("place")
//                        .name(placeListDto.getPlaceName())
//                        .build());
                }
            }

        return searchSuggestionDtoList;

    }

    /**
     * 유저가 좋아요를 누른 시설(place)들을 DTO List로 반환하는 메서드 입니다.
     * //     * @param memberEmail LikePlaceEntity 에서 유저(member)가 좋아요를 누른 place 정보들을 가져옵니다.
     *
     * @return List<PlaceDto> PlaceDto형태로 반환합니다(검색창에서 검색했을때와 동일하게 view로 전송하기 위함입니다.)
     */
//    @Transactional
//    @Override
//    public List<PlaceDto> getPlaceDtosByMemberEmail(String memberEmail) {
//        List<MemberLikePlace> memberLikePlaceEntityByMemberEmail = memberLikePlaceService.getMemberLikePlaceEntityByMemberEmail(memberEmail);
//        List<PlaceDto> placeDtos = memberLikePlaceEntityByMemberEmail.stream().map(memberLikePlace -> {
//            PlaceDto placeDto = new PlaceDto();
//            return placeDto.convertToPlaceDto(memberLikePlace.getPlace());
//        }).collect(Collectors.toList());
//
//        return placeDtos;
////        return null;
//    }
    public PlaceDto convertPlaceDto(Place place) {
        PlaceDto placeDto = new PlaceDto();
        placeDto.convertToPlaceDto(place);
        return placeDto;
    }

//    public void saveXmlToDataBase(String filePath) {
////        PlaceServiceImpl placeService = new PlaceServiceImpl();
////        placeService.convertAddressToCoordinates("경기도 화성시 삼성전자로 1 (반월동)");
//
////        String filePath = "C:\\Users\\tksek\\OneDrive\\바탕 화면\\git\\data\\xml1.xml"; // XML 파일 경로
//
//        try {
//            File inputFile = new File(filePath);
//            //xml 파싱을 위한 인스턴스 형성
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            //XML 파싱하여 Document 객체로 변환
//            Document doc = dBuilder.parse(inputFile);
//            doc.getDocumentElement().normalize();
//
//            NodeList nodeList = doc.getElementsByTagName("item");
//            for (int temp = 0; temp < nodeList.getLength(); temp++) {
//                Node node = nodeList.item(temp);
//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//                    Element element = (Element) node;
//                    String data1 = element.getElementsByTagName("data1").item(0).getTextContent();
//                    String data2 = element.getElementsByTagName("data2").item(0).getTextContent();
//                    log.info("data1={}",data1);
//                    log.info("data2={}",data2);
//                    // 여기서부터 데이터베이스에 INSERT하는 코드를 작성합니다.
//                    // data1과 data2를 사용하여 데이터베이스에 INSERT합니다.
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void convertAddressToCoordinates(String address) throws IOException {
        String url = KAKAO_API_URL.replace("{address}", address);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // Parse JSON response using Jackson ObjectMapper
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode documentsNode = rootNode.get("documents");
        JsonNode firstResultNode = documentsNode.get(0);
        double latitude = firstResultNode.get("y").asDouble();
        double longitude = firstResultNode.get("x").asDouble();
        log.info("latitue={},longtitude={}", latitude, longitude);
    }

    @Transactional
    public void parsXmlFile() {
        try {
            String filePath = "C:\\Users\\tksek\\OneDrive\\test1\\전국_요양병원_list.xml"; // XML 파일 경로
            File inputFile = new File(new String(filePath.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            //xml 파싱을 위한 인스턴스 형성
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //XML 파싱하여 Document 객체로 파싱
            Document doc = builder.parse(inputFile);
//            doc.getDocumentElement().normalize();

            //모든 <item> 요소 nodeList 가져오기
            NodeList nodeList = doc.getElementsByTagName("item");
            log.info("nodeLength={}", nodeList.getLength());
            for (int i = 1; i < nodeList.getLength(); i++) {
                log.info(i + "번째 기관정보");
                Element item = (Element) nodeList.item(i);
                //list에서 원하는 tag의 값들 추출해서 로깅
                savePlaceFromXmlData(item, i);
            }
            if (nodeList.getLength() == 0) {
                // <XPos> 요소가 없는 경우 메시지 출력
                System.out.println("<XPos> 요소를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // element에서 요소에서 필요한 데이터를 추출하여 출력하는 메서드
    private void savePlaceFromXmlData(Element element, int index) {
        String xPos = Optional.ofNullable(getTagTextContent(element, "XPos")).orElse("1");

        // 위도(latitude)
        String yPos = Optional.ofNullable(getTagTextContent(element, "YPos")).orElse("1");
        // 기관명
        String yadmNm = Optional.ofNullable(getTagTextContent(element, "yadmNm")).orElse("정보없음");

        // 전화번호
        String telno = Optional.ofNullable(getTagTextContent(element, "telno")).orElse("1");

        // 시,도 정보
        String sidoCdNm = Optional.ofNullable(getTagTextContent(element, "sidoCdNm")).orElse("정보없음");

        // 시군구 정보
        String sgguCdNm = Optional.ofNullable(getTagTextContent(element, "sgguCdNm")).orElse("정보없음");

        // 업종정보
        String clCdNm = Optional.ofNullable(getTagTextContent(element, "clCdNm")).orElse("정보없음");

        // 도로명 주소
        String addr = Optional.ofNullable(getTagTextContent(element, "addr")).orElse("정보없음");

        //업종별 정보(ex,종합병원, 요양병원 등)
        log.info("시도정보={},시군구정보={},위도={},경도={},도로명주소={}", sidoCdNm, sgguCdNm, yPos, xPos, addr);
        log.info("시설명={},업종정보={},전화번호={}", yadmNm, clCdNm, telno);

        SubRegion foundSubRegion = subRegionService.getSubRegionByRegionNameAndSubRegionName(sidoCdNm, sgguCdNm);
        //Xml Data로 PlaceDto 정보 할당.
        PlaceCreateDto placeCreateDto = createPlaceSaveDto(yadmNm, addr, yPos, xPos);
        //Data 업종 정보로 DB에서 Data조회 data 꺼내옴
        Optional<Category> foundCategory = categoryRepository.findByCategoryName(clCdNm);
        //Place entity 저장 메서드 호출
        savePlace(placeCreateDto, foundCategory.get(), foundSubRegion);
    }

    private String getTagTextContent(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return "0"; // 또는 다른 기본값을 반환할 수 있습니다.
        }


    }


}


//    private static final String kakaoUrl= "https://dapi.kakao.com/v2/local/search/address.json";
//
//    public void convertToGeo(String address){
//        // 수정된 부분: "KakaoAK "와 kakaoApiKey 사이에 공백을 추가하여 올바른 API 키 형식으로 만듦
//        String apiKey = "KakaoAK " + kakaoApiKey;
//        // Request URL 주소 할당
//        String convertUrl = kakaoUrl + "?query=" + address;
//        // httpheader set
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", apiKey);
//        // set HTTP entity
//        HttpEntity<String> entity = new HttpEntity<>("parameters", httpHeaders);
//        // Send GET request
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(convertUrl, HttpMethod.GET, entity, String.class);
////        // 수정된 부분: response가 null이거나 response.getBody()가 null이 아닌지 확인하여 NullPointerException 방지
////        if (response != null && response.getBody() != null) {
////            // Parse JSON response using org.json library
////            ObjectMapper objectMapper = new ObjectMapper();
////            try {
////                JsonNode root = objectMapper.readTree(response.getBody());
////                JsonNode documents = root.path("documents");
////                // Assuming the first result is the most relevant
////                if (documents.isArray() && documents.size() > 0) {
////                    JsonNode firstResult = documents.get(0);
////                    double latitude = firstResult.path("y").asDouble();
////                    double longitude = firstResult.path("x").asDouble();
////                    log.info("latitude={},longitude={}", latitude, longitude);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        } else {
////            log.error("No response or empty response body");
////        }
//    }
//




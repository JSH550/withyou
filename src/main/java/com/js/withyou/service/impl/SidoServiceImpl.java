package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Sido.SidoDto;
import com.js.withyou.data.dto.Sido.SidoNameDto;
import com.js.withyou.data.dto.Sigungu.SigunguDto;
import com.js.withyou.data.entity.Region.Sido;
import com.js.withyou.data.entity.Region.Sigungu;
import com.js.withyou.repository.SidoRepository;
import com.js.withyou.repository.SigunguRepository;
import com.js.withyou.service.SidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j

public class SidoServiceImpl implements SidoService {
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;


    public SidoServiceImpl(SidoRepository sidoRepository, SigunguRepository sigunguRepository) {
        this.sidoRepository = sidoRepository;
        this.sigunguRepository = sigunguRepository;
    }

    //Controller에서 regionId, subRegionName으로 새로운 sigungu 형성
    public SigunguDto saveSigunguWithSido(Long sidoId, String sigunguName) {
        Optional<Sido> foundSido = sidoRepository.findById(sidoId);
        if (isSigunguNameUnique(sigunguName) == false) {
            throw new IllegalArgumentException("중복된 세부 지역이름입니다. 이름을 확인해 주세요");//중복된 이름이 있으면 exception 처리
        } else if (foundSido.isEmpty()) {
            throw new NoSuchElementException("Sido 존재하지 않습니다.");//세부지역을 담을 지역명이 없으면 exception 처리
        } else {
            Sigungu savedSigungu = sigunguRepository.save(Sigungu.createSigungu(sigunguName, foundSido.get()));
            log.info("id={},name={},Sido={}", savedSigungu.getSigunguId(), savedSigungu.getSigunguName(), savedSigungu.getSido().getSidoName());
            return convertToSigunguDto(savedSigungu);
        }

    }

    /**
     * 주어진 키워드를 포함하는 하위 지역(Sigungu)을 검색하여 해당 결과를 SigunguDto 리스트로 반환합니다.
     * 시군구 조회에는 1회의 쿼리가 실행됩니다. (시도 정보는 JOIN FETCH를 통해 함께 로드됩니다.)
     * 장소 조회에도 1회의 쿼리가 실행됩니다.
     *
     * @param keyword 검색에 사용할 키워드
     * @return 키워드를 포함하는 하위 지역(Sigungu)의 목록을 SigunguDto 형태로 반환합니다.
     */
    @Transactional
    @Override
    public List<SigunguDto> findSigunguByKeyword(String keyword) {
        List<Sigungu> bySigunguNameContaining = sigunguRepository.findBySigunguNameContaining(keyword);
        List<SigunguDto> sigunguDtos = bySigunguNameContaining.stream()
                .map(Sigungu -> {
                    SigunguDto sigunguDto = new SigunguDto();
                    return sigunguDto.convertToSigunguDto(Sigungu);
                }).collect(Collectors.toList());
        return sigunguDtos;
    }

    @Override
    public List<SidoNameDto> getAllSidoNameDtoList() {
        //DB에서 sido 테이블의 모든 레코드를 찾아옵니다.
        List<Sido> sidoList = sidoRepository.findAll();

      return  sidoList.stream()
                .map(Sido -> SidoNameDto.builder().sidoId(Sido.getSidoId())
                        .sidoName(Sido.getSidoName())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<SidoDto> getAllSidoList() {
        List<Sido> allSidos = sidoRepository.findAll();
        ArrayList<SidoDto> sidoDtoList = new ArrayList<>();
        for (Sido sido : allSidos) {
            sidoDtoList.add(convertToRegionDto(sido));
        }
        return sidoDtoList;
    }

    @Override
    public SidoDto getSidoById(Long sidoId) {
        if (sidoId == null) {
            throw new IllegalArgumentException("Sido Id값이 없습니다. 값을 확인해주세요");
        }
        Optional<Sido> region = sidoRepository.findById(sidoId);
        return convertToRegionDto(region.get());
    }

    @Override
    public SidoDto getSidoDtoBySidoShortName(String sidoShortName) {

        Optional<Sido> byRegionShortName = sidoRepository.findSidoBySidoShortName(sidoShortName);
        return convertToRegionDto(byRegionShortName.get());
    }

    //검색어로 시도 검색하여 List에 저장, 반환해주는 메서드
    @Transactional
    @Override
    public List<SidoDto> getSidoDtoListByKeyword(String keyword) {
        List<Sido> foundSidoList = sidoRepository.findSidoListWithSigungusAndPlacesByNameContaining(keyword);
        //검색어로 지역이름 검색 결과가 없을시, 검색어로 시도 축약어 검색
        if (foundSidoList.isEmpty()) {
            List<Sido> foundSidoListByShortName = sidoRepository.findSidoBySidoShortNameContaining(keyword);
            return foundSidoListByShortName.stream()
                    .map(this::convertToRegionDto)
                    .collect(Collectors.toList());
        }

        //Sido 을 DTO로 변환하여 List에 저장후 return
        return foundSidoList.stream()
                //Sido entity를 RegionDto형태로 변환
                .map(this::convertToRegionDto)
                //stream에서 수집된 요소를 list형태로 반환
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SidoNameDto> getSidoNameDtoListByKeyword(String keyword) {
        List<Sido> sidos = sidoRepository.findByKeywordContaining(keyword);
        List<SidoNameDto> sidoNameDtos = sidos.stream().map(Sido -> SidoNameDto.builder()
                .sidoId(Sido.getSidoId())
                .sidoName(Sido.getSidoName())
                .build()).collect(Collectors.toList());

        return sidoNameDtos;


    }


    @Transactional
    @Override
    public List<SigunguDto> getSigungusBySidoNameContainingKeyword(String keyword) {
        return sidoRepository.findSigungusBySidoNameContaining(keyword).stream()
                .map(Sigungu -> {
                    SigunguDto sigunguDto = new SigunguDto();
                    return sigunguDto.convertToSigunguDto(Sigungu);
                }).collect(Collectors.toList());
    }


    public SidoDto convertToRegionDto(Sido sido) {
        SidoDto sidoDto = new SidoDto();
        sidoDto.setSidoId(sido.getSidoId());
        sidoDto.setSidoName(sido.getSidoName());
        sidoDto.setSidoShortName(sido.getSidoShortName());
        //Sigungu DTO로 변환해서 저장
        List<SigunguDto> sigunguDtoList = sido.getSigungus().stream().map(sigungu -> {
                    SigunguDto sigunguDto = new SigunguDto();
                    return sigunguDto.convertToSigunguDto(sigungu);
                }
        ).collect(Collectors.toList());
        sidoDto.setSigungus(sigunguDtoList);

        return sidoDto;
    }

    public SigunguDto convertToSigunguDto(Sigungu sigungu) {
        SigunguDto sigunguDto = new SigunguDto();
        sigunguDto.setSigunguId(sigungu.getSigunguId());
        sigunguDto.setSigunguName(sigungu.getSigunguName());
        //Place entity를 DTO로 변환하여 List에 저장
//        List<PlaceDto> placeDtos = sigungu.getPlaces().stream().map(place -> {
//            PlaceDto placeDto = new PlaceDto();
//            return placeDto.convertToPlaceDto(place);
//        }).collect(Collectors.toList());
//        sigunguDto.setPlaces(placeDtos);
        sigunguDto.setSidoName(sigunguDto.getSidoName());
        return sigunguDto;
    }

    //sigunguName이중복되었는지 확인하는 메서드
    public boolean isSigunguNameUnique(String sigunguName) {


        Optional<Sigungu> foundSigungu = sigunguRepository.findBySigunguName(sigunguName);
        if (foundSigungu.isEmpty()) {
            return true;//table에서 sigungu이 없으면 true 반환
        } else {

            return false;//table에 중복된값이 있으면 false 반환
        }


    }


}

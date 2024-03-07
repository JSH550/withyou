package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.SubRegion.SubRegionDto;
import com.js.withyou.data.dto.place.PlaceDto;
import com.js.withyou.data.entity.Region;
import com.js.withyou.data.entity.SubRegion;
import com.js.withyou.repository.RegionRepository;
import com.js.withyou.repository.SubRegionRepository;
import com.js.withyou.service.RegionService;
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

public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final SubRegionRepository subRegionRepository;

    public RegionServiceImpl(RegionRepository regionRepository, SubRegionRepository subRegionRepository) {
        this.regionRepository = regionRepository;
        this.subRegionRepository = subRegionRepository;
    }

    //Controller에서 regionId, subRegionName으로 새로운 subRegion 형성
    public SubRegionDto saveSubRegionWithRegion(Long regionId, String subRegionName) {
        Optional<Region> foundRegion = regionRepository.findById(regionId);
        if (isSubRegionNameUnique(subRegionName) == false) {
            throw new IllegalArgumentException("중복된 세부 지역이름입니다. 이름을 확인해 주세요");//중복된 이름이 있으면 exception 처리
        } else if (foundRegion.isEmpty()) {
            throw new NoSuchElementException("Region 존재하지 않습니다.");//세부지역을 담을 지역명이 없으면 exception 처리
        } else {
            SubRegion savedSubRegion = subRegionRepository.save(SubRegion.createSubRegion(subRegionName, foundRegion.get()));
            log.info("id={},name={},Region={}", savedSubRegion.getSubRegionId(), savedSubRegion.getSubRegionName(), savedSubRegion.getRegion().getRegionName());
            return convertToSubRegionDto(savedSubRegion);
        }

    }

    /**
     * 주어진 키워드를 포함하는 하위 지역(SubRegion)을 검색하여 해당 결과를 SubRegionDto 리스트로 반환합니다.
     * 시군구 조회에는 1회의 쿼리가 실행됩니다. (시도 정보는 JOIN FETCH를 통해 함께 로드됩니다.)
     * 장소 조회에도 1회의 쿼리가 실행됩니다.
     *
     * @param keyword 검색에 사용할 키워드
     * @return 키워드를 포함하는 하위 지역(SubRegion)의 목록을 SubRegionDto 형태로 반환합니다.
     */
    @Transactional
    @Override
    public List<SubRegionDto> findSubregionByKeyword(String keyword) {
        List<SubRegion> bySubRegionNameContaining = subRegionRepository.findBySubRegionNameContaining(keyword);
        List<SubRegionDto> subRegionDtos = bySubRegionNameContaining.stream()
                .map(subRegion -> {
                    SubRegionDto subRegionDto = new SubRegionDto();
                    return subRegionDto.convertToSubRegionDto(subRegion);
                }).collect(Collectors.toList());
        return subRegionDtos;
    }


    @Override
    public List<RegionDto> getAllRegions() {
        List<Region> allRegions = regionRepository.findAll();
        ArrayList<RegionDto> regionDtoList = new ArrayList<>();
        for (Region region : allRegions) {
            regionDtoList.add(convertToRegionDto(region));
        }
        return regionDtoList;
    }

    @Override
    public RegionDto getRegionById(Long RegionId) {
        if (RegionId == null) {
            throw new IllegalArgumentException("Region Id값이 없습니다. 값을 확인해주세요");
        }
        Optional<Region> region = regionRepository.findById(RegionId);
        return convertToRegionDto(region.get());
    }

    @Override
    public RegionDto getRegionByRegionShortName(String RegionShortName) {

        Optional<Region> byRegionShortName = regionRepository.findByRegionShortName(RegionShortName);
        return convertToRegionDto(byRegionShortName.get());
    }

    //검색어로 시도 검색하여 List에 저장, 반환해주는 메서드
    @Transactional
    @Override
    public List<RegionDto> getRegionByKeyword(String keyword) {
        List<Region> foundRegionList = regionRepository.findRegionsWithSubRegionsAndPlacesByNameContaining(keyword);
        //검색어로 지역이름 검색 결과가 없을시, 검색어로 시도 축약어 검색
        if (foundRegionList.isEmpty()) {
            List<Region> foundRegionListByShortName = regionRepository.findByRegionShortNameContaining(keyword);
            return foundRegionListByShortName.stream()
                    .map(this::convertToRegionDto)
                    .collect(Collectors.toList());
        }

        //Region 을 DTO로 변환하여 List에 저장후 return
        return foundRegionList.stream()
                //Region entity를 RegionDto형태로 변환
                .map(this::convertToRegionDto)
                //stream에서 수집된 요소를 list형태로 반환
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<SubRegionDto> findSubRegionsByRegionNameContainingKeyword(String keyword) {
        return regionRepository.findSubRegionsByRegionNameContaining(keyword).stream()
                .map(SubRegion-> {
                    SubRegionDto subRegionDto = new SubRegionDto();
                   return subRegionDto.convertToSubRegionDto(SubRegion);
                }).collect(Collectors.toList());
    }


    public RegionDto convertToRegionDto(Region region) {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegionId(region.getRegionId());
        regionDto.setRegionName(region.getRegionName());
        regionDto.setRegionShortName(region.getRegionShortName());
        //SubRegion DTO로 변환해서 저장
        List<SubRegionDto> subRegionDtoList = region.getSubRegions().stream().map(subregion -> {
                    SubRegionDto subRegionDto = new SubRegionDto();
                    return subRegionDto.convertToSubRegionDto(subregion);
                }
        ).collect(Collectors.toList());
        regionDto.setSubRegions(subRegionDtoList);

        return regionDto;
    }

    public SubRegionDto convertToSubRegionDto(SubRegion subregion) {
        SubRegionDto subRegionDto = new SubRegionDto();
        subRegionDto.setSubRegionId(subregion.getSubRegionId());
        subRegionDto.setSubRegionName(subregion.getSubRegionName());
        //Place entity를 DTO로 변환하여 List에 저장
        List<PlaceDto> placeDtos = subregion.getPlaces().stream().map(place -> {
            PlaceDto placeDto = new PlaceDto();
            return placeDto.convertToPlaceDto(place);
        }).collect(Collectors.toList());
        subRegionDto.setPlaces(placeDtos);
        subRegionDto.setRegionName(subRegionDto.getRegionName());
        return subRegionDto;
    }

    //subRegionName이중복되었는지 확인하는 메서드
    public boolean isSubRegionNameUnique(String subRegionName) {


        Optional<SubRegion> foundSubRegion = subRegionRepository.findBySubRegionName(subRegionName);
        if (foundSubRegion.isEmpty()) {
            return true;//table에서 subRegion이 없으면 true 반환
        } else {

            return false;//table에 중복된값이 있으면 false 반환
        }


    }


}

package com.js.withyou.service.impl;

import com.js.withyou.data.dto.Region.RegionDto;
import com.js.withyou.data.dto.Region.SubRegionDto;
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
    public SubRegionDto saveSubRegionWithRegion(Long regionId, String subRegionName){
        Optional<Region> foundRegion = regionRepository.findById(regionId);
        if(isSubRegionNameUnique(subRegionName) ==false){
            throw new IllegalArgumentException("중복된 세부 지역이름입니다. 이름을 확인해 주세요");//중복된 이름이 있으면 exception 처리
        }else if(foundRegion.isEmpty()) {
            throw new NoSuchElementException("Region 존재하지 않습니다.");//세부지역을 담을 지역명이 없으면 exception 처리
        } else {
            SubRegion savedSubRegion= subRegionRepository.save(SubRegion.createSubRegion(subRegionName,foundRegion.get()));
            log.info("id={},name={},Region={}",savedSubRegion.getSubRegionId(),savedSubRegion.getSubRegionName(),savedSubRegion.getRegion().getRegionName());
            return convertToSubRegionDto(savedSubRegion);
        }

    }


    @Override
    public List<RegionDto> getAllRegions() {
        List<Region> allRegions = regionRepository.findAll();
        ArrayList<RegionDto> regionDtoList= new ArrayList<>();
        for (Region region : allRegions) {
            regionDtoList.add(convertToRegionDto(region));
        }
        return regionDtoList;
    }

    @Override
    public RegionDto getRegionById(Long RegionId) {
        if (RegionId==null){
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


    public RegionDto convertToRegionDto(Region region){
        RegionDto regionDto = new RegionDto();
        regionDto.setRegionId(region.getRegionId());
        regionDto.setRegionName(region.getRegionName());
        regionDto.setRegionShortName(region.getRegionShortName());
        return regionDto;
    }

    public SubRegionDto convertToSubRegionDto(SubRegion subregion){
        SubRegionDto subRegionDto = new SubRegionDto();
        subRegionDto.setSubRegionId(subregion.getSubRegionId());
        subRegionDto.setSubRegionName(subregion.getSubRegionName());
        subRegionDto.setRegionName(subRegionDto.getRegionName());
            return subRegionDto;
    }
    
    //subRegionName이중복되었는지 확인하는 메서드
    public boolean isSubRegionNameUnique(String subRegionName){


        Optional<SubRegion> foundSubRegion = subRegionRepository.findBySubRegionName(subRegionName);
            if (foundSubRegion.isEmpty()){
                return true;//table에서 subRegion이 없으면 true 반환
            }else {
                
                return false;//table에 중복된값이 있으면 false 반환
            }



    }

}

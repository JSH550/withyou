package com.js.withyou.repository;

import com.js.withyou.data.entity.Region.DongRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DongRegionRepository extends JpaRepository<DongRegion,Long> {


     List<DongRegion> findDongRegionsBySubRegionSubRegionId(Long subRegionId);




}

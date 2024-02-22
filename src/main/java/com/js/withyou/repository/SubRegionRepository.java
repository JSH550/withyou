package com.js.withyou.repository;

import com.js.withyou.data.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubRegionRepository extends JpaRepository<SubRegion,Long> {

    Optional<SubRegion> findBySubRegionName(String subRegionName);





}

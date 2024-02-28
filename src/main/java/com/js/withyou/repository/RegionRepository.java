package com.js.withyou.repository;

import com.js.withyou.data.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findByRegionShortName(String RegionShrotName);


}

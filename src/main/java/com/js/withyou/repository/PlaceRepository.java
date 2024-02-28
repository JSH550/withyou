package com.js.withyou.repository;

import com.js.withyou.data.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Long> {
List<Place> findByPlaceNameContaining(String keyword);


}

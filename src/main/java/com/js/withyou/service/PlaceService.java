package com.js.withyou.service;

import com.js.withyou.data.dto.PlaceSaveDto;
import com.js.withyou.data.entity.Category;
import com.js.withyou.data.entity.SubRegion;

public interface PlaceService {
    void parsXmlFile();

//    void savePlace();

    void savePlace(PlaceSaveDto placeSaveDto, Category category, SubRegion subRegion);
}

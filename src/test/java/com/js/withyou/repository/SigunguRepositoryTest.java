package com.js.withyou.repository;

import com.js.withyou.data.entity.Region.Sigungu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SigunguRepositoryTest {

    private final SigunguRepository sigunguRepository;

    @Autowired
    public SigunguRepositoryTest(SigunguRepository sigunguRepository) {
        this.sigunguRepository = sigunguRepository;
    }

    @Test
    public void findSubRegionByRegionRegionIdTest(){
        List<Sigungu> sigunguId = sigunguRepository.findSigunguBySidoSidoId(2L);
        for (Sigungu sigungu : sigunguId) {
            System.out.println(sigungu.getSigunguName());

        }


    }

}
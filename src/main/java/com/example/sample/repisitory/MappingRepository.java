package com.example.sample.repisitory;

import com.example.sample.entity.Goods;
import com.example.sample.entity.Mapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MappingRepository extends CrudRepository<Mapping,Long> {


    // 브랜드의 매핑된 상 ID 조회
    public List<Mapping> findByBrandId(Long brandId);
}

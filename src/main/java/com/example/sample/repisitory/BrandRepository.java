package com.example.sample.repisitory;

import com.example.sample.entity.Brand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends CrudRepository<Brand,Long> {

    @Override
    public List<Brand> findAll();

    // 브랜드 ID 조회
    public List<Brand> findByBrandId(Long brandId);

    // 브랜드명으로 조회
    public Brand findByBrandName(String brandName);

    // 브랜드명으로 조회
    public Brand findTop1ByOrderByTotalpriceAsc();

}

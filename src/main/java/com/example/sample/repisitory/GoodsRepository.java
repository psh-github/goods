package com.example.sample.repisitory;

import com.example.sample.entity.Goods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends CrudRepository<Goods,Long> {

    // 전체조회
    @Override
    public List<Goods> findAll();

    // 상품 ID 조회
    public Goods findByGoodsId(Long goodsId);

    // 케테고리와 브랜드ID가 같은 상품 조회
    public Goods findByCategoryAndBrandId(String Category, Long BrandId);

    // 카테고리 별 상품 조회
    public List<Goods> findByCategory(String Category);

    // 브랜드 별 상품 조회
    public List<Goods> findByBrandId(Long brandId);
}

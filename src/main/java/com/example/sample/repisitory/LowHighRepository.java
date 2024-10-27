package com.example.sample.repisitory;

import com.example.sample.entity.LowHigh;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LowHighRepository extends CrudRepository<LowHigh,Long> {

    @Override
    public List<LowHigh> findAll();

    // 카테고리별 상품 리스트 조회
    public List<LowHigh> findByCategory(String category);


    // 최저가/최고가 상품 리스트 조회
    public List<LowHigh> findByLowhighDevide(String lowhighDevide);


    // 카테고리의 최대값 / 최소값 삭제
    public int deleteByCategory(String category);
}

package com.example.sample.controller;


import com.example.sample.dto.GoodsDTO;
import com.example.sample.service.SampleService;
import com.example.sample.vo.GoodsInfoVO;
import com.example.sample.vo.GoodsVO;
import com.example.sample.vo.LowPirceGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SampleContoller {

    @Autowired
    private SampleService sampleService;

    /**
     * 상품 리스트 조회
     *
     * @return 조회된 상품 리스트
     */
    @RequestMapping(value = "/findAllGoodsList",method = RequestMethod.GET)
    public List<GoodsVO> findAllGoodsList() {
        return sampleService.findAllGoodsList();
    }

    /**
     * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     *
     * @return
     */
    @RequestMapping(value = "/findLowPriceBrandSet",method = RequestMethod.GET)
    public Map<String,Object> findLowPriceBrandSet() {
       return  sampleService.findLowPriceBrandSet();
    }

    /**
     * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     *
     * @return
     */
    @RequestMapping(value = "/findCategoryLowHighGoodsList",method = RequestMethod.GET)
    public Map<String,Object> findCategoryLowHighGoodsList(@RequestParam String category) {
        return sampleService.findCategoryLowHighGoodsList(category);
    }
}

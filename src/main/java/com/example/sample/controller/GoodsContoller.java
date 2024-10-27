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
@RequestMapping("goods")
public class GoodsContoller {

    // category - 카테고리
    // goods - 상품
    // brand - 브랜드
    @Autowired
    private SampleService sampleService;

    /**
     * 기초데이터 삽입
     */
    @RequestMapping(value = "/initData",method = RequestMethod.GET)
    public void initData() {
        sampleService.initData();
    }

    /**
     * 상품 리스트 조회
     *
     * @return 조회된 상품 리스트
     */
    @RequestMapping(value = "/findAllList",method = RequestMethod.GET)
    public List<GoodsVO> findAllGoodsList() {
        return sampleService.findAllGoodsList();
    }

    /**
     * 카테고리별 상품 리스트 (삭제 예정)
     *
     * @param category
     * @return
     */
    @RequestMapping(value = "/findCategoryGoodsList",method = RequestMethod.GET)
    public List<GoodsVO> findCategoryGoodsList(@RequestParam String category) {
        return sampleService.findCategoryGoodsList(category);
    }

    /**
     * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     *
     * @return
     */
    @RequestMapping(value = "/findLowPriceSet",method = RequestMethod.GET)
    public LowPirceGoodsVO findLowPriceGoodsSet() {
        return sampleService.findLowPriceGoodsSet();
    }

    /**
     * 브랜드 및 상품을 추가 API
     *
     * @return
     */
    @RequestMapping(value = "/saveGoods",method = RequestMethod.POST)
    public boolean saveGoods(@RequestBody GoodsDTO goods) {
        return  sampleService.saveGoods(GoodsInfoVO.builder()
                        .category(goods.getCategory())
                        .price(goods.getPrice())
                        .brandName(goods.getBrandName())
                .build());
    }

    /**
     * 브랜드 및 상품을 수정 API(수정중)
     *
     * @return
     */
    @RequestMapping(value = "/modifyGoods",method = RequestMethod.POST)
    public boolean modifyGoods(@RequestBody GoodsDTO goods) {
        return  sampleService.modifyGoods(GoodsInfoVO.builder()
                        .goodsId(goods.getGoodsId())
                        .category(goods.getCategory())
                        .price(goods.getPrice())
                        .brandName(goods.getBrandName())
                .build());
    }

    /**
     * 브랜드 및 상품을 삭제 API
     *
     * @return
     */
    @RequestMapping(value = "/removeGoods",method = RequestMethod.DELETE)
    public boolean removeGoods(@RequestBody GoodsDTO goods) {
        return  sampleService.removeGoods(GoodsInfoVO.builder()
                        .goodsId(goods.getGoodsId())
                .build());
    }
}

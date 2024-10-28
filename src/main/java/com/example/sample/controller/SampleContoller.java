package com.example.sample.controller;


import com.example.sample.dto.GoodsDTO;
import com.example.sample.service.SampleService;
import com.example.sample.vo.GoodsInfoVO;
import com.example.sample.vo.GoodsVO;
import com.example.sample.vo.LowPirceGoodsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SampleContoller {

    @Autowired
    private SampleService sampleService;



    /**
         * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
         *
         * @return 조회한 결과
         */
        @Operation(summary = "구현1", description = "카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API")
        @RequestMapping(value = "/findLowPriceSet",method = RequestMethod.GET)
        public ModelMap findLowPriceGoodsSet() {
            ModelMap modelMap = new ModelMap();
            try {
                LowPirceGoodsVO lowPirceGoodsVO = sampleService.findLowPriceGoodsSet();
                modelMap.addAttribute("data", lowPirceGoodsVO);

            } catch (Exception e) {
                modelMap.addAttribute("status", false);
                modelMap.addAttribute("error", e.getMessage());
            }

            return modelMap;
        }

        /**
         * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
         *
         * @return 조회한 결과
         */
        @Operation(summary = "구현2", description = "단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API")
        @RequestMapping(value = "/findLowPriceBrandSet",method = RequestMethod.GET)
        public ModelMap findLowPriceBrandSet() {

            ModelMap modelMap = new ModelMap();
            try {
                Map<String,Object> brandSet = sampleService.findLowPriceBrandSet();
                modelMap.addAttribute("data", brandSet);

            } catch (Exception e) {
                modelMap.addAttribute("status", false);
                modelMap.addAttribute("error", e.getMessage());
            }

            return modelMap;
        }

        /**
         * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
         *
         * @return 조회한 결과
         */
        @Operation(summary = "구현3", description = "카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API")
        @Parameter(name = "category", description = "카테고리명", example = "상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리 중 1개")
        @RequestMapping(value = "/findCategoryLowHighGoodsList",method = RequestMethod.GET)
        public Map<String,Object> findCategoryLowHighGoodsList(@RequestParam String category) {

            ModelMap modelMap = new ModelMap();
            try {
                Map<String,Object> brandSet = sampleService.findCategoryLowHighGoodsList(category);
                modelMap.addAttribute("data", brandSet);

            } catch (Exception e) {
                modelMap.addAttribute("status", false);
                modelMap.addAttribute("error", e.getMessage());
            }

            return modelMap;
        }


        /**
         * 브랜드 및 상품을 추가 API
         *
         * @return 등록여부
         */
        @Operation(summary = "구현4-저장", description = "브랜드 및 상품을 추가 API")
        @RequestMapping(value = "/saveGoods",method = RequestMethod.POST)
        @Parameter(name = "상품정보", description = "{카테고리, 브랜드명, 가격}", example = "{\"category\":\"상의\",\"brandName\":\"Z\",\"price\":\"100000\"}")
        public ModelMap saveGoods(@RequestBody GoodsDTO goods) {
            ModelMap modelMap = new ModelMap();
           try {
               boolean result = sampleService.saveGoods(GoodsInfoVO.builder()
                                           .category(goods.getCategory())
                                           .price(goods.getPrice())
                                           .brandName(goods.getBrandName())
                                   .build());
               modelMap.addAttribute("result", result);

           } catch (Exception e) {
               modelMap.addAttribute("status", false);
               modelMap.addAttribute("error", e.getMessage());
           }

           return modelMap;
        }

        /**
         * 브랜드 및 상품을 수정 API
         *
         * @return 수정여부
         */
        @Operation(summary = "구현4-수정", description = "브랜드 및 상품을 수정 API")
        @RequestMapping(value = "/modifyGoods",method = RequestMethod.POST)
        @Parameter(name = "상품정보", description = "{상품ID, 카테고리, 가격}", example = "{\"goodsId\":73,\"category\":\"상의\",\"price\":\"500000\"}")
        public ModelMap modifyGoods(@RequestBody GoodsDTO goods) {
            ModelMap modelMap = new ModelMap();
           try {
               boolean result = sampleService.modifyGoods(GoodsInfoVO.builder()
                                           .goodsId(goods.getGoodsId())
                                           .category(goods.getCategory())
                                           .price(goods.getPrice())
                                           .brandName(goods.getBrandName())
                                   .build());
               modelMap.addAttribute("result", result);

           } catch (Exception e) {
               modelMap.addAttribute("status", false);
               modelMap.addAttribute("error", e.getMessage());
           }

           return modelMap;
        }

        /**
         * 브랜드 및 상품을 삭제 API
         *
         * @return 삭제여부
         */
        @Operation(summary = "구현4-삭제", description = "브랜드 및 상품을 삭제 API")
        @RequestMapping(value = "/removeGoods",method = RequestMethod.DELETE)
        @Parameter(name = "상품정보", description = "{상품ID}", example = "{\"goodsId\":\"73\"}")
        public ModelMap removeGoods(@RequestBody GoodsDTO goods) {

            ModelMap modelMap = new ModelMap();
           try {
               boolean result = sampleService.removeGoods(GoodsInfoVO.builder()
                                           .goodsId(goods.getGoodsId())
                                   .build());
               modelMap.addAttribute("result", result);

           } catch (Exception e) {
               modelMap.addAttribute("status", false);
               modelMap.addAttribute("error", e.getMessage());
           }

           return modelMap;
        }
}

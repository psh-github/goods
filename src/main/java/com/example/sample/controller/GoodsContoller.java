package com.example.sample.controller;


import com.example.sample.dto.GoodsDTO;
import com.example.sample.service.SampleService;
import com.example.sample.vo.GoodsInfoVO;
import com.example.sample.vo.GoodsVO;
import com.example.sample.vo.LowPirceGoodsVO;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "기초 데이터 삽입", description = "예제 데이터 입력")
    @RequestMapping(value = "/initData",method = RequestMethod.GET)
    public void initData() {
        sampleService.initData();
    }

    /**
     * 상품 리스트 조회
     *
     * @return 조회된 상품 리스트
     */
    @Operation(summary = "상품 리스트 조회", description = "등록된 모든 상품 리스트 조회")
    @RequestMapping(value = "/findAllList",method = RequestMethod.GET)
    public List<GoodsVO> findAllGoodsList() {
        return sampleService.findAllGoodsList();
    }
}

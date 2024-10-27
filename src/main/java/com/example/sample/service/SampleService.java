package com.example.sample.service;

import com.example.sample.entity.Brand;
import com.example.sample.entity.Goods;
import com.example.sample.entity.LowHigh;
import com.example.sample.entity.Mapping;
import com.example.sample.repisitory.BrandRepository;
import com.example.sample.repisitory.GoodsRepository;
import com.example.sample.repisitory.LowHighRepository;
import com.example.sample.repisitory.MappingRepository;
import com.example.sample.vo.GoodsInfoVO;
import com.example.sample.vo.GoodsVO;
import com.example.sample.vo.LowPirceGoodsVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SampleService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private MappingRepository mappingRepository;

    @Autowired
    private LowHighRepository lowHighRepository;

    @Transactional
    public int initData () {

        int rRow = 0;

        /////////// 상품
        List<GoodsInfoVO> goodsList = init();

        // 브랜드 등록 후 브랜드별 상품 등록-> 매핑테이블 등록 -> 브랜드 총 금액
        // 브랜드

        goodsList.stream().forEach(g -> {
            System.out.println(g.toString());
            saveGoods(g);
        });


//

        return rRow;
    }

    /**
     * 상품 등록
     *
     * @param goodsInfoVO 상품정보
     * @return 등록 성공여부
     */
    @Transactional
    public boolean saveGoods(GoodsInfoVO goodsInfoVO) {

        boolean result = false;

        try {

            Brand brand = brandRepository.findByBrandName(goodsInfoVO.getBrandName());
            if (brand == null) {
                brand = brandRepository.save(new Brand(null, goodsInfoVO.getBrandName(),0L));
            }
            Goods goods = goodsRepository.save(new Goods(null, goodsInfoVO.getCategory(),brand.getBrandId(),goodsInfoVO.getPrice()));
            mappingRepository.save(new Mapping(null, brand.getBrandId(),goods.getGoodsId()));

            // 카테고리별 최소값 / 최대값 계산
            loghighFunc(goodsInfoVO.getCategory());

            // 여기서 브랜드 총금액 계산
            List<Goods> brandGoodsList = goodsRepository.findByBrandId(brand.getBrandId());

            Long totalPirce = brandGoodsList.stream().map(Goods::getPrice).reduce(0L, Long::sum);

            Brand brandInfo = brandRepository.findById(brand.getBrandId()).orElse(null);

            if (brandInfo != null) {
                brandInfo.setTotalprice(totalPirce);
            }

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;

    }

    /**
     * 상품 수정
     *
     * @param goodsInfoVO
     * @return
     */
    @Transactional
    public boolean modifyGoods(GoodsInfoVO goodsInfoVO) {
        boolean result = false;

        List<String> categoryList = new ArrayList<>();

        Goods goods = goodsRepository.findByGoodsId(goodsInfoVO.getGoodsId());
        if (goods != null) {
            // 카테고리 변경
            if (!goodsInfoVO.getCategory().equals(goods.getCategory())) {
                // 1. 기존 카테고리와 같으면 변경하지 않음o
                // 2. 기존 카테고리와 다를경우o
                // 기존 카테고리 lowhigh 재산정
                // 수정된 카테고리 lowhign 재산정8
                categoryList.add(new String(goods.getCategory()));
                categoryList.add(goodsInfoVO.getCategory());
                if(!goods.getCategory().equals(goodsInfoVO.getCategory())) {
                    goods.setCategory(goodsInfoVO.getCategory());
                }

            }

            // 금액변경o
            if (goodsInfoVO.getPrice() != null) {
                if(!categoryList.contains(goodsInfoVO.getCategory())) {
                    categoryList.add(goods.getCategory());
                }
                goods.setPrice(goodsInfoVO.getPrice());
                // 카테고리의 lowhigh 재산정
            }

            // 브랜드 변경
            if (goodsInfoVO.getBrandName() != null) {
                // 1.기존 브랜드와 같은경우 수정하지 않음o
                // 2. 기존 브랜드와 다른경우o
                // 2-1. 브랜드 변경(없는경우 새로 생성)o
                Brand brand = brandRepository.findByBrandName(goodsInfoVO.getBrandName());
                if (brand == null) {
                    brand = brandRepository.save(new Brand(null, goodsInfoVO.getBrandName(),goodsInfoVO.getPrice()));
                }
                goods.setBrandId(brand.getBrandId());

            }

            // 카테고리 재산정
            categoryList.stream().forEach(c->{
                loghighFunc(goodsInfoVO.getCategory());
            });



            result = true;
        }

        return result;
    }

    /**
     * 상품 삭제
     *
     * @param goodsInfoVO
     * @return
     */
    public boolean removeGoods(GoodsInfoVO goodsInfoVO) {
        boolean result = false;

        Goods goods = goodsRepository.findByGoodsId(goodsInfoVO.getGoodsId());
        if (goods != null) {
            goodsRepository.delete(goods);

            loghighFunc(goodsInfoVO.getCategory());
            result = true;
        }

        return result;
    }

    /**
     * 전체 상품 조회
     *
     * @return
     */
    public List<GoodsVO> findAllGoodsList() {

        List<Goods> goodsList = goodsRepository.findAll();

        return goodsList.stream().map(g->{
         return GoodsVO.builder()
                 .goodsId(g.getGoodsId())
                 .category(g.getCategory())
                 .brandId(g.getBrandId())
                 .price(g.getPrice())
                 .build();
        }).collect(Collectors.toList());

    }

    /**
     * 카테고리별 상품 리스트
     *
     * @param category
     * @return
     */
    public List<GoodsVO> findCategoryGoodsList(String category) {

        List<Goods> goodsList = goodsRepository.findByCategory(category);

        return goodsList.stream().map(g->{
         return GoodsVO.builder()
                 .goodsId(g.getGoodsId())
                 .category(g.getCategory())
                 .brandId(g.getBrandId())
                 .price(g.getPrice())
                 .build();
        }).collect(Collectors.toList());
    }

    /**
     * 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     *
     * @return 카테고리 별 최저가격 브랜드와 상품 리스트 및 총액
     */
    public LowPirceGoodsVO findLowPriceGoodsSet() {

        List<Goods> goodsList = new ArrayList<>();
        Long totalPrice = 0L;
        try {

            // 최저가 목록
            List<LowHigh> lowPriceList = lowHighRepository.findByLowhighDevide("L");

            // 브랜드 리스트
            List<Brand> brandList = brandRepository.findAll();

            // 최저가 상품 정보 조회
            lowPriceList.stream().forEach(l->{
                Goods goods = goodsRepository.findById(l.getGoodsId()).orElse(null);
                if (goods != null) {goodsList.add(goods);}
            });

            // 총액
            totalPrice = goodsList.stream().map(Goods::getPrice).reduce(0L, Long::sum);

            DecimalFormat df = new DecimalFormat("###,###");

            return LowPirceGoodsVO.builder()
                            .totalPrice(totalPrice)
                            .goodsList(goodsList.stream().map(g->LowPirceGoodsVO.goodsList.builder()
                                            .category(g.getCategory())
                                            .brandName(brandList.stream().filter(b->b.getBrandId() == g.getBrandId()).findFirst().map(Brand::getBrandName).orElse(""))
                                            .price(df.format(g.getPrice()))
                                            .build()).collect(Collectors.toList()))
                            .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return LowPirceGoodsVO.builder()
                .totalPrice(totalPrice)
                .build();
    }

    /**
     * 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
     *
     * @return
     */
    public Map<String,Object> findLowPriceBrandSet() {

        Brand brand = brandRepository.findTop1ByOrderByTotalpriceAsc();

        List<Goods> goodsList = goodsRepository.findByBrandId(brand.getBrandId());

        Long totalPrice = goodsList.stream().map(Goods::getPrice).reduce(0L, Long::sum);

        DecimalFormat df = new DecimalFormat("###,###");

        Map<String,Object> lowMap = new HashMap<>();
        Map<String,Object> brandMap = new HashMap<>();
        brandMap.put("브랜드",brand.getBrandName());
        brandMap.put("카테고리",goodsList.stream().map(g->{
            Map<String, String> cMap = new HashMap<>();
            cMap.put("카테고리",g.getCategory());
            cMap.put("가격",df.format(g.getPrice()));
            return cMap;
        }).collect(Collectors.toList()));

        brandMap.put("총액",df.format(totalPrice));

        lowMap.put("최저가",brandMap);

        return lowMap;
    }

    /**
     * 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     *
     * @return
     */
    public Map<String,Object> findCategoryLowHighGoodsList(String category) {

        List<LowHigh> lowHigh = lowHighRepository.findByCategory(category);

        List<Brand> brandList = brandRepository.findAll();
        Map<String,Object> lowMap = new HashMap<>();
        lowMap.put("카테고리",category);

        DecimalFormat df = new DecimalFormat("###,###");
        lowHigh.stream().forEach(l->{
            Goods goods = goodsRepository.findByGoodsId(l.getGoodsId());
            Map<String,String> goodsMap = new HashMap<>();
            goodsMap.put("브랜드",brandList.stream().filter(b->b.getBrandId().equals(goods.getBrandId())).map(Brand::getBrandName).findFirst().orElse(""));
            goodsMap.put("가격",df.format(goods.getPrice()));
            lowMap.put(l.getLowhighDevide().equals("L")?"최저가":"최고가",goodsMap);
        });

        return lowMap;
    }

    public void loghighFunc(String category) {
        List<Goods> goodsList = goodsRepository.findByCategory(category);

                    // 최대값/최소값 카테고리 삭제
                    lowHighRepository.deleteByCategory(category);

                    List<GoodsInfoVO> goodsInfoVOList = goodsList.stream().map(g->GoodsInfoVO.builder()
                                    .goodsId(g.getGoodsId())
                                    .category(g.getCategory())
                                    .brandId(g.getBrandId())
                                    .price(g.getPrice())
                                    .build()).collect(Collectors.toList());

                    goodsInfoVOList.sort(Comparator.comparing(GoodsInfoVO::getPrice));

                    if (goodsInfoVOList.size() > 0) {
                        GoodsInfoVO lowPirce = goodsInfoVOList.get(0);
                        lowHighRepository.save(new LowHigh(null, lowPirce.getCategory(),lowPirce.getGoodsId(),"L"));
                        if (goodsInfoVOList.size() > 1) {
                            GoodsInfoVO highPirce = goodsInfoVOList.get(goodsInfoVOList.size()-1);
                            lowHighRepository.save(new LowHigh(null, highPirce.getCategory(),highPirce.getGoodsId(),"H"));
                        }
                    }
    }


    private List<GoodsInfoVO> init() {

        List<GoodsInfoVO> goodsList = new ArrayList<>();

        // 상의
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("A").price(11200L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("B").price(10500L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("C").price(10000L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("D").price(10100L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("E").price(10700L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("F").price(11200L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("G").price(10500L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("H").price(10800L).build());
        goodsList.add(GoodsInfoVO.builder().category("상의").brandName("I").price(11400L).build());

        // 아우터
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("A").price(5500L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("B").price(5900L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("C").price(6200L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("D").price(5100L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("E").price(5000L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("F").price(7200L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("G").price(5800L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("H").price(6300L).build());
        goodsList.add(GoodsInfoVO.builder().category("아우터").brandName("I").price(6700L).build());

        // 바지
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("A").price(4200L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("B").price(3800L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("C").price(3300L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("D").price(3000L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("E").price(3800L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("F").price(4000L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("G").price(3900L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("H").price(3100L).build());
        goodsList.add(GoodsInfoVO.builder().category("바지").brandName("I").price(3200L).build());

        // 스니커즈
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("A").price(9000L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("B").price(9100L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("C").price(9200L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("D").price(9500L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("E").price(9900L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("F").price(9300L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("G").price(9000L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("H").price(9700L).build());
        goodsList.add(GoodsInfoVO.builder().category("스니커즈").brandName("I").price(9500L).build());

        // 가방
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("A").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("B").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("C").price(2200L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("D").price(2500L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("E").price(2300L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("F").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("G").price(2200L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("H").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("가방").brandName("I").price(2400L).build());

        // 모자
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("A").price(1700L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("B").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("C").price(1900L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("D").price(1500L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("E").price(1800L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("F").price(1600L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("G").price(1700L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("H").price(1600L).build());
        goodsList.add(GoodsInfoVO.builder().category("모자").brandName("I").price(1700L).build());

        // 양말
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("A").price(1800L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("B").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("C").price(2200L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("D").price(2400L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("E").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("F").price(2300L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("G").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("H").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("양말").brandName("I").price(1700L).build());

        // 액세서리
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("A").price(2300L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("B").price(2200L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("C").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("D").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("E").price(2100L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("F").price(1900L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("G").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("H").price(2000L).build());
        goodsList.add(GoodsInfoVO.builder().category("액세서리").brandName("I").price(2400L).build());


        return goodsList;
    }

}

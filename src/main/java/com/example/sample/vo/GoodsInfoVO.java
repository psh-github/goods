package com.example.sample.vo;


import com.example.sample.entity.Goods;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class GoodsInfoVO {

    private Long goodsId;
    private String category;
    private Long brandId;
    private String brandName;
    private Long price;
}

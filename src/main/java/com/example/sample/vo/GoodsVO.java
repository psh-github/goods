package com.example.sample.vo;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class GoodsVO {

    private Long goodsId;
    private String category;
    private Long brandId;
    private Long price;
}

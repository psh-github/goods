package com.example.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GoodsDTO {
    private Long goodsId;
    private String category;
    private String brandName;
    private Long price;
}

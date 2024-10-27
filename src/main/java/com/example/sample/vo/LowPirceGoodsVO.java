package com.example.sample.vo;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class LowPirceGoodsVO {

    private Long totalPrice;
    private List<goodsList> goodsList;


    @Setter
    @Getter
    @Builder
    public static class goodsList {
        private String category;
        private String brandName;
        private String price;
    }
}

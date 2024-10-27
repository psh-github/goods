package com.example.sample.vo;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class BrandVO {

    private Long brandId;
    private String brandName;
    private Long totalprice;
}

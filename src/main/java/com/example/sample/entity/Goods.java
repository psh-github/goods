package com.example.sample.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue
    private Long goodsId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long brandId;

    @Column(nullable = false)
    private Long price;


}

package com.example.sample.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "low_high")
public class LowHigh {

    @Id
    @GeneratedValue
    private Long lowhighId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long goodsId;

    @Column(nullable = false)
    private String lowhighDevide;

}

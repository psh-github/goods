package com.example.sample.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "mapping")
public class Mapping {

    @Id
    @GeneratedValue
    private Long mappingId;

    @Column(nullable = false)
    private Long goodsId;

    @Column(nullable = false)
    private Long brandId;

}

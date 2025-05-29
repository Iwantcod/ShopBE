package com.example.shopPJT.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity(name = "Orders")
@Getter
public class Order {
    @Id @GeneratedValue
    private Long id;
}

package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "delivery_address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private String customerName;
    private String customerPhoneNum;
    private String recipientName;
    private String recipientPhoneNum;
    private String zipcode;
    private String address;
    private String detailAddress;

}

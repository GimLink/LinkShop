package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "order_payment_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderPaymentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_payment_info_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column
    private String payType;

    @Column
    private String bank;

    @Column
    private String customerName;

    @Column
    private String accountNumber;

    @Column
    private String dueDate;

    @Column
    private String company;

    @Column
    private String number;
}

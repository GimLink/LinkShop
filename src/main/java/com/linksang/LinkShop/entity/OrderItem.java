package com.linksang.LinkShop.entity;


import com.linksang.LinkShop.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column
    private String wayBillNum;

    @Column
    private int quantity;

    @Column
    private int totalPrice;

}

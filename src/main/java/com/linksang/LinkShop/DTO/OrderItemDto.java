package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.DeliveryAddress;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.enums.DeliveryStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long id;
    private Order order;
    private Item item;
    private DeliveryStatus deliveryStatus;
    private String wayBillNum;
    private int quantity;
    private int totalPrice;
    private DeliveryAddress deliveryAddress;
}

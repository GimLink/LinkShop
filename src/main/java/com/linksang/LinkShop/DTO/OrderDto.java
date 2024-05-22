package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.OrderItem;
import com.linksang.LinkShop.entity.OrderPaymentInformation;
import com.linksang.LinkShop.enums.PayType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Member member;
    private Delivery delivery;
    private List<OrderItem> orderItems;
    private OrderPaymentInformation paymentInfo;
    private PayType payType;
    private int totalPrice;
    private String orderNum;
    private LocalDateTime createdDate;
    private LocalDateTime depositDate;
}

package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Order;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentInfoDto {

    private Long id;
    private Order order;
    private String payType;
    private String bank;
    private String customerName;
    private String accountNumber;
    private String dueDate;
    private String company;
    private String number;
}

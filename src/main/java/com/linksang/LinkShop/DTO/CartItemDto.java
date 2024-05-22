package com.linksang.LinkShop.DTO;

import com.linksang.LinkShop.entity.Cart;
import com.linksang.LinkShop.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;
    private Cart cart;
    private Item item;
    private int quantity;
    private int totalPrice;
}

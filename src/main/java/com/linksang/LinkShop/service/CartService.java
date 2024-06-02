package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.CartItemDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Cart;
import com.linksang.LinkShop.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {

    void createCart(Member member);

    void addCart(Long id, int quantity);

    Cart save(Cart cart);

    List<CartItemDto> findCartItems(Member member);

    List<OrderItemDto> cartItemToPayment(String cartIdList, HttpSession session);

    void deleteCartItem(Long id);

    void deleteCartItemAll(List<Long> id);

    void updateQuantity(Long id, int quanntity);
}

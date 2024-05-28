package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Cart;
import com.linksang.LinkShop.entity.CartItem;
import com.linksang.LinkShop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndItem(Cart cart, Item item);

    List<CartItem> findByCartId(Long cartId);
}

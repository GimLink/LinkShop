package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Table(name = "cart_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ColumnDefault("0")
    private int quantity;

    @Column
    private int totalPrice;

    public static CartItem createCartItem(Cart cart, Item item, int quantity) {

        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.item = item;
        cartItem.quantity = quantity;
        return cartItem;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;}

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}

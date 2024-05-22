package com.linksang.LinkShop.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
    private List<CartItem> cartItemList = new ArrayList<CartItem>();

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void createCart(Member member) {
        this.member = member;
        member.setCart(this);
    }
}

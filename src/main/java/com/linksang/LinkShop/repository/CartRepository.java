package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Cart;
import com.linksang.LinkShop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMember(Member member);
}

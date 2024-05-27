package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.repository.custom.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    List<Order> findAllByMember(Member member);

    Optional<Order> findByOrderNum(String orderNum);

    boolean existsByOrderNum(String orderNum);
}

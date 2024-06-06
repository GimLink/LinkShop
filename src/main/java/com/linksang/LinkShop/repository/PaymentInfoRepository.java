package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.OrderPaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<OrderPaymentInformation, Long> {
}

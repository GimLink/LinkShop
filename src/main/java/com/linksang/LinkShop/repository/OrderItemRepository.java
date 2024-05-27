package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.OrderItem;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.repository.custom.OrderitemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderitemRepositoryCustom {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);
}

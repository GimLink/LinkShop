package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Long countByDeliveryStatus(DeliveryStatus deliveryStatus);
}

package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.enums.DeliveryStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> searchAllByMember(Long lastOrderId, Member member);

    List<OrderDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);
}

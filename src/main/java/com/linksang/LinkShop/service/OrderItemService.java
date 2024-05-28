package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.OrderItem;
import com.linksang.LinkShop.enums.DeliveryStatus;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderItemService {

    Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto);

    OrderItem findById(Long id);

    List<OrderItemDto> searchAll(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable);

    List<OrderItemDto> searchAllByMember(Member member);

    List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable);

    boolean existsOrderItem(String orderNum, Long orderItemId);

    void save(OrderItem orderItem);

    void saveWayBillNum(Long orderItemId, String wayBillNum);

    boolean changeOrderItemStatus(Long orderItemId, DeliveryStatus deliveryStatus);
}

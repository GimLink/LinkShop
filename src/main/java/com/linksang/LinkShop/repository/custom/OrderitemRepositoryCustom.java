package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.DeliveryStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderitemRepositoryCustom {

    Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto);

    List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchAll(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable);

    List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable);

    List<OrderItemDto> searchAllByMember(Member member);
}

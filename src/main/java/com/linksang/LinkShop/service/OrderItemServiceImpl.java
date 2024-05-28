package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.entity.OrderItem;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.exception.OrderNotFoundException;
import com.linksang.LinkShop.repository.OrderItemRepository;
import com.linksang.LinkShop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto) {
        return orderItemRepository.searchTotal(deliveryStatus, searchDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문상품입니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchAll(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable) {
        return orderItemRepository.searchAll(deliveryStatus, searchDto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchAllByMember(Member member) {
        return orderItemRepository.searchAllByMember(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {

        List<OrderItemDto> orderItemList = orderItemRepository.searchByDeliveryStatus(deliveryStatus, pageable);
        for (OrderItemDto orderItem : orderItemList) {
            orderItem.setDeliveryAddress(orderItem.getOrder().getDelivery().getDeliveryAddress());
        }
        return orderItemList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable) {
        return orderItemRepository.searchAllByDeliveryStatusAndSearchDto(deliveryStatus, searchDto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsOrderItem(String orderNum, Long orderItemId) {

        Order order = orderRepository.findByOrderNum(orderNum)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문번호입니다."));
        return order.getOrderItems().stream().anyMatch(o -> o.getId().equals(orderItemId));
    }

    @Override
    @Transactional
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    public void saveWayBillNum(Long orderItemId, String wayBillNum) {

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderNotFoundException("존재하지 않는 주문입니다."));

        orderItem.setWayBillNum(wayBillNum); //송장번호
        orderItem.setDeliveryStatus(DeliveryStatus.DELIVERY_ING);

    }

    @Override
    @Transactional
    public boolean changeOrderItemStatus(Long orderItemId, DeliveryStatus deliveryStatus) {

        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemId);
        if (orderItem.isPresent()) {
            orderItem.get().setDeliveryStatus(deliveryStatus);
            return true;
        }
        return false;
    }
}

package com.linksang.LinkShop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.entity.OrderPaymentInformation;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.repository.DeliveryRepository;
import com.linksang.LinkShop.repository.ItemRepository;
import com.linksang.LinkShop.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;


    @Override
    public Long getLastOrderId(List<OrderDto> orderList, Long lastOrder) {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public Order findByOrderNum(String orderNum) {
        return null;
    }

    @Override
    public Long countByDeliveryStatus(DeliveryStatus status) {
        return null;
    }

    @Override
    public String createOrderNum() {
        return null;
    }

    @Override
    public List<OrderItemDto> itemToPayment(String itemList) {
        return null;
    }

    @Override
    public ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderNum, int amount) throws Exception {
        return null;
    }

    @Override
    public OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode) {
        return null;
    }

    @Override
    public String doOrder(HttpSession session, OrderPaymentInformation paymentInfo, Delivery delivery) {
        return null;
    }

    @Override
    public Model getModelPayInfo(Order order, Model model) {
        return null;
    }

    @Override
    public List<OrderDto> searchAllByMember(Long lastOrderId, Member member) {
        return null;
    }

    @Override
    public void updateOrderToDepositSuccess(String orderNum) {

    }

    @Override
    public List<OrderDto> searchByDeliveryStatus(DeliveryStatus status) {
        return null;
    }
}

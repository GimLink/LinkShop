package com.linksang.LinkShop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.entity.OrderPaymentInformation;
import com.linksang.LinkShop.enums.DeliveryStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

public interface OrderService {

    Long getLastOrderId(List<OrderDto> orderList, Long lastOrder);

    Order findById(Long id);

    Order findByOrderNum(String orderNum);

    Long countByDeliveryStatus(DeliveryStatus status);

    String createOrderNum();

    List<OrderItemDto> itemToPayment(String itemList);

    ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderNum, int amount) throws Exception;

    OrderPaymentInformation getVirtualAccountInfo(JsonNode successNode);

    String doOrder(HttpSession session, OrderPaymentInformation paymentInfo, Delivery delivery);

    Model getModelPayInfo(Order order, Model model);

    List<OrderDto> searchAllByMember(Long lastOrderId, Member member);

    void updateOrderToDepositSuccess(String orderNum);

    List<OrderDto> searchByDeliveryStatus(DeliveryStatus status);
}

package com.linksang.LinkShop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.*;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.exception.ItemNotFoundException;
import com.linksang.LinkShop.exception.OrderNotFoundException;
import com.linksang.LinkShop.repository.DeliveryRepository;
import com.linksang.LinkShop.repository.ItemRepository;
import com.linksang.LinkShop.repository.OrderRepository;
import com.linksang.LinkShop.util.CommonService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final MemberService memberService;
    private final CommonService commonService;

    ModelMapper mapper = new ModelMapper();

    @Value("${toss.payment.test_secret_api_key")
    String SECRET_KEY;


    @Override
    public Long getLastOrderId(List<OrderDto> orderList, Long lastOrder) {

        if (orderList.isEmpty()) {
            return lastOrder;
        } else {
            return orderList.stream()
                    .min(Comparator.comparingLong(OrderDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {

        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findByOrderNum(String orderNum) {

        return orderRepository.findByOrderNum(orderNum).orElseThrow(()->new OrderNotFoundException("해당 주문번호는 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByDeliveryStatus(DeliveryStatus status) {

        return deliveryRepository.countByDeliveryStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public String createOrderNum() {

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String random = String.valueOf(commonService.randomNum());

        String orderNum = date + random;
        boolean result = orderRepository.existsByOrderNum(orderNum);

        if (!result) {
            return orderNum;
        }
        return createOrderNum();
    }

    @Override
    @Transactional
    public List<OrderItemDto> itemToPayment(String itemList) {

        JsonElement jsonElement = JsonParser.parseString(itemList);
        JsonArray jsonElements = jsonElement.getAsJsonArray();
        JsonObject jsonItem = (JsonObject) jsonElements.get(0);

        Long itemId = Long.parseLong(jsonItem.get("itemId").getAsString());
        int quantity = Integer.parseInt(jsonItem.get("quantity").getAsString());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품은 존재하지 않습니다. 상품번호 : " + itemId));

        OrderItemDto itemDto = OrderItemDto.builder()
                .item(item)
                .quantity(quantity)
                .totalPrice(item.getPrice() * quantity)
                .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                .build();

        List<OrderItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(itemDto);

        return itemDtoList;
    }

    @Override
    public ResponseEntity<JsonNode> tossPayment(String paymentKey, String orderNum, int amount) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SECRET_KEY, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //body
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderNum);
        payloadMap.put("amount", String.valueOf(amount));

        //header + body
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        //post
        return restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
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

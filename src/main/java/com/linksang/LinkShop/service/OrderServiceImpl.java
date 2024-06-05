package com.linksang.LinkShop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linksang.LinkShop.DTO.AddressDto;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.OrderPaymentInfoDto;
import com.linksang.LinkShop.entity.*;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.enums.PayType;
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
import org.springframework.data.domain.Pageable;
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
        JsonObject jsonItem = (JsonObject) jsonElements.get(0); //상품 페이지에서 바로 구매시 사용하는 메서드라 0번째 하나만 있으면 ok

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

        JsonNode virtualAccount = successNode.get("virtualAccount");

        return OrderPaymentInformation.builder()
                .payType(PayType.VIRTUAL_ACCOUNT.getTitle())
                .customerName(virtualAccount.get("customerName").asText())
                .accountNumber(virtualAccount.get("accountnumber").asText())
                .bank(virtualAccount.get("bank").asText())
                .dueDate(virtualAccount.get("dueDate").asText())
                .build();
    }

    @Override
    @Transactional
    public String doOrder(HttpSession session, OrderPaymentInformation paymentInfo, Delivery delivery) {

        //배송정보 가져오기
        String orderNum = (String) session.getAttribute("orderNum");
        String orderName = (String) session.getAttribute("orderName");
        List<OrderItemDto> itemList = (List<OrderItemDto>) session.getAttribute("orderItems");
        List<Long> cartItemIdList = (List<Long>) session.getAttribute("cartItemIdList");
        AddressDto addressDto = (AddressDto) session.getAttribute("addressDto");
        PayType payType = (PayType) session.getAttribute("payType");

        //order 객체 만들기용
        Member member = memberService.getCurrentMember();
        List<OrderItem> orderItems = itemList.stream()
                .map(i -> mapper.map(i, OrderItem.class)).toList();

        //배송정보
        delivery.setDeliveryAddress(mapper.map(addressDto, DeliveryAddress.class));
        delivery.setOrderName(orderName);

        //주문
        Order order = Order.createOrder(member, delivery, orderItems, payType, paymentInfo, orderNum);
        orderRepository.save(order);

        //장바구니에서 주문시 장바구니서 삭제
        if (cartItemIdList != null) {
            cartService.deleteCartItemAll(cartItemIdList);
            session.removeAttribute("cartItemId");
        }

        //세션에서 주문정보 삭제
        session.removeAttribute("orderNum");
        session.removeAttribute("orderName");
        session.removeAttribute("orderItems");
        session.removeAttribute("addressDto");
        session.removeAttribute("payType");
        session.removeAttribute("totalPrice");

        return orderNum;
    }

    @Override
    @Transactional(readOnly = true)
    public Model getModelPayInfo(Order order, Model model) {
        mapper.getConfiguration().setAmbiguityIgnored(true);

        OrderPaymentInfoDto payInfo = mapper.map(order.getPaymentInfo(), OrderPaymentInfoDto.class);
        OrderDto orderInfo = mapper.map(order, OrderDto.class);
        AddressDto address = mapper.map(order.getDelivery().getDeliveryAddress(), AddressDto.class);
        List<OrderItemDto> orderItems = order.getOrderItems().stream()
                .map(i -> mapper.map(i, OrderItemDto.class)).toList();

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("payInfo", payInfo);
        model.addAttribute("address", address);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("method", payInfo.getPayType());

        return model;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> searchAllByMember(Long lastOrderId, Member member) {

        List<Order> orderList = orderRepository.searchAllByMember(lastOrderId, member);

        return orderList.stream().map(i-> mapper.map(i, OrderDto.class)).toList();
    }

    @Override
    @Transactional
    public void updateOrderToDepositSuccess(String orderNum) {

        Order order = orderRepository.findByOrderNum(orderNum)
                .orElseThrow(() -> new OrderNotFoundException("해당 주문은 존재하지 않습니다."));
        order.getDelivery().setDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);
        order.setDepositDate(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> searchByDeliveryStatus(DeliveryStatus status, Pageable pageable) {

        return orderRepository.searchByDeliveryStatus(status, pageable);
    }
}

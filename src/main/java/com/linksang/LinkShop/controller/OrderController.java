package com.linksang.LinkShop.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.linksang.LinkShop.DTO.AddressDto;
import com.linksang.LinkShop.DTO.CallbackPayload;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Delivery;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.entity.OrderPaymentInformation;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.enums.PayType;
import com.linksang.LinkShop.enums.TossPayments;
import com.linksang.LinkShop.exception.ParameterNotfoundException;
import com.linksang.LinkShop.service.CartService;
import com.linksang.LinkShop.service.OrderService;
import com.linksang.LinkShop.util.CommonService;
import com.linksang.LinkShop.util.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CommonService commonService;

    @GetMapping("/order/checkout")
    @ApiOperation(value = "주문 페이지로 이동", notes = "문제 발생해서 나갔다가 결제중이던 주문페이지로 다시 이동해야 할 때")
    public String checkoutPage(HttpSession session, Model model) {

        if (session.getAttribute("orderNum") == null) {
            return "error/notOrder";
        }

        model.addAttribute("addressDto", session.getAttribute("addressDto"));
        model.addAttribute("orderItems", session.getAttribute("orderItems"));
        model.addAttribute("orderNum", session.getAttribute("orderNum"));
        model.addAttribute("orderName", session.getAttribute("orderName"));
        model.addAttribute("totalPrice", session.getAttribute("totalPrice"));
        return "order/order_checkout";
    }

    @PostMapping("/order/checkout")
    @ApiOperation(value = "구매할 상품 선택후 주문페이지로 이동")
    public String checkoutPage(String itemList, String where, Model model, HttpSession session) {

        List<OrderItemDto> orderItems = new ArrayList<>();
        String orderName = "";
        int totalPrice = 0;

        if (where.equals("product")) { //상품 페이지서 바로 구매시
            orderItems = orderService.itemToPayment(itemList);
            orderName = orderItems.get(0).getItem().getItemName();
            totalPrice += orderItems.get(0).getTotalPrice();

        } else if (where.equals("cart")) { //장바구니에서 구매
            orderItems = cartService.cartItemToPayment(itemList, session);
            orderName = orderItems.get(0).getItem().getItemName() + "외" + (orderItems.size() - 1) + "건";
            for (OrderItemDto orderItem : orderItems) {
                totalPrice += orderItem.getTotalPrice();
            }
        } else {
            throw new ParameterNotfoundException("where값 확인 필요.");
        }

        String orderNum = orderService.createOrderNum();

        session.setAttribute("orderItems", orderItems);
        session.setAttribute("orderNum", orderNum);
        session.setAttribute("orderName", orderName);
        session.setAttribute("totalPrice", totalPrice);

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("orderNum", orderNum);
        model.addAttribute("orderName", orderName);
        model.addAttribute("totalPrice", totalPrice);

        return "order/order_checkout";
    }

    @RequestMapping("/order/virtual-account/callback")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "가상계좌 입금 완료시 처리", notes = "토스 가상계좌에 입금 완료 콜백 url")
    public void paymentCheck(@RequestBody CallbackPayload callbackPayload) {

        if (callbackPayload.getStatus().equals(TossPayments.DONE.getValue())) {
            orderService.updateOrderToDepositSuccess(callbackPayload.getOrderId());
        } else if (callbackPayload.getStatus().equals(TossPayments.CANCELED.getValue())) {
            log.info("취소");
        }
    }

    @PostMapping("/order/virtualAccount")
    @ApiOperation(value = "가상계좌 결제")
    public @ResponseBody String saveAddressDto(@Validated(ValidationSequence.class) AddressDto addressDto, BindingResult errors, String payMethod, HttpSession session) {

        PayType payType = PayType.findByPayType(payMethod);

        if (payType.getTitle().equals("없음")) {
            return "fail";
        } else if (errors.hasErrors()) {
            return commonService.getErrorMessage(errors);
        }

        session.setAttribute("addressDto", addressDto);
        session.setAttribute("payType", payType);
        return "success";
    }

    @RequestMapping("/success")
    @ApiOperation(value = "토스페이먼츠 가상계좌 결제")
    public String confirmPayment(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount,
                                 Model model, HttpSession session) throws Exception {

        //토스 가상계좌 승인 요청
        ResponseEntity<JsonNode> responseEntity = orderService.tossPayment(paymentKey, orderId, amount);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            OrderPaymentInformation paymentInfo = orderService.getVirtualAccountInfo(responseEntity.getBody());
            Delivery delivery = new Delivery();
            delivery.setDeliveryStatus(DeliveryStatus.DEPOSIT_READY);

            String orderNum = orderService.doOrder(session, paymentInfo, delivery);

            Order order = orderService.findByOrderNum(orderNum);
            model.addAttribute(orderService.getModelPayInfo(order, model));

            return "order/order_success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/order_fail";
        }
    }
}

package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.MemberDto;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.repository.ItemRepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final MemberService memberService;
    private final CommonService commonService;
    private final SecurityService security;

    @GetMapping("admin/main")
    @ApiOperation(value = "관리자 메인 페이지")
    public String adminMain(Model model) {

        Pageable pageable = PageRequest.ofSize(3);
        Long ingOrderItemTotal = orderItemService.searchTotal(DeliveryStatus.DELIVERY_ING, new SearchDto());
        Long depositOrderTotal = orderService.countByDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);

        List<OrderItemDto> ingOrderItems = orderItemService.searchByDeliveryStatus(DeliveryStatus.DELIVERY_ING, pageable);
        List<OrderDto> depositOrders = orderService.searchByDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS, pageable);
        List<MemberDto> members = memberService.findAll(pageable);

        model.addAttribute("ingOrderItems", ingOrderItems);
        model.addAttribute("depositOrders", depositOrders);
        model.addAttribute("members", members);

        model.addAttribute("ingOrderItemTotal", ingOrderItemTotal);
        model.addAttribute("depositOrderTotal", depositOrderTotal);

        return "admin/admin_main";
    }

    @GetMapping("/admin/register")
    @ApiOperation(value = "상품 등록 페이지")
    public String itemRegister() {
        return "admin/admin_registerItem";
    }
}

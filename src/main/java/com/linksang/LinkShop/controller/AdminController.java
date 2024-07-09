package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.*;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.repository.ItemRepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import com.linksang.LinkShop.util.PaginationShowSizeTen;
import com.linksang.LinkShop.util.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
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
        return "/admin/admin_registerItem";
    }

    @GetMapping("/admin/itemList")
    @ApiOperation(value = "상품 목록 페이지")
    public String itemListPage(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model) {

        Long totalPost = itemService.searchTotal(searchDto.getItemName(), searchDto.getCategory(), searchDto.getSaleStatus());
        PaginationShowSizeTen page = new PaginationShowSizeTen(totalPost, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<ItemDto> items = itemService.searchAll(searchDto, pageable);

        model.addAttribute("page", page);
        model.addAttribute("items", items);
        model.addAttribute("searchDto", searchDto);

        return "admin/admin_itemList";
    }

    @GetMapping("/admin/send/orderItem")
    @ApiOperation(value = "상품 배송 페이지")
    public String sendOrderItemPage(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model) {

        Long total = orderItemService.searchTotal(DeliveryStatus.DELIVERY_READY, searchDto);
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<OrderItemDto> orderItems = orderItemService.searchAllByDeliveryStatusAndSearchDto(DeliveryStatus.DELIVERY_READY, searchDto, pageable);

        model.addAttribute("page", page);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("searchDto", searchDto);
        return "admin/admin_sendOrderItem";
    }

    @GetMapping("/admin/orderList")
    @ApiOperation(value = "주문 상품 목록", notes = "배송상태를 입금완료에서 배송중으로 바꾸는 페이지")
    public String depositItemList(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model) {

        DeliveryStatus deliveryStatus = DeliveryStatus.findByDeliveryStatus(searchDto.getDeliveryStatus());

        Long total = orderItemService.searchTotal(deliveryStatus, searchDto);
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowPageNum());
        List<OrderItemDto> orderItems = orderItemService.searchAll(deliveryStatus, searchDto, pageable);

        model.addAttribute("page", page);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("searchDto", searchDto);
        return "admin/admin_orderList";
    }

    @PatchMapping("/admin/deliveryStatus/change")
    @ApiOperation(value = "주문 상태 변경")
    public @ResponseBody String deliveryStatusChange(@RequestParam Long orderItemId, @RequestParam DeliveryStatus deliveryStatus) {

        boolean result = orderItemService.changeOrderItemStatus(orderItemId, deliveryStatus);
        if (!result) {
            return "fail";
        }

        return "success";
    }

    @PostMapping("/admin/send/orderItem")
    @ApiOperation(value = "운송장 번호 입력후 상품 배송 처리", notes = "상품 배송")
    public @ResponseBody String deliveryItem(Long orderItemId, String orderNUm, String wayBillNum) {

        boolean result = orderItemService.existsOrderItem(orderNUm, orderItemId);
        if (!result) {
            return "fail";
        }

        orderItemService.saveWayBillNum(orderItemId, wayBillNum);
        return "success";
    }

    @PostMapping("/admin/item/register")
    @ApiOperation(value = "관리자 페이지에서 상품 등록")
    public @ResponseBody String itemSave(MultipartHttpServletRequest mtfRequest, @Validated(ValidationSequence.class) ItemDto itemDto, BindingResult errors) throws IOException {

        if (errors.hasErrors()) {
            return commonService.getErrorMessage(errors);
        } else if (!security.checkHasRole(Role.ADMIN.getValue())) {
            return "role";
        }

        itemService.saveItem(mtfRequest, itemDto);
        return "success";
    }

    @DeleteMapping("/admin/item/delete")
    @ApiOperation(value = "관리자페이지에서 상품 삭제")
    public @ResponseBody String itemDelete (@RequestParam List <Long> itemIdList) {
        itemRepository.deleteAllById(itemIdList);
        return "success";
    }
}

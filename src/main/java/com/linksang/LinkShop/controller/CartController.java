package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.CartDto;
import com.linksang.LinkShop.DTO.CartItemDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.exception.CartNotFoundException;
import com.linksang.LinkShop.service.CartService;
import com.linksang.LinkShop.service.MemberService;
import com.linksang.LinkShop.util.CommonService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final CommonService commonService;

    @GetMapping("/cart")
    public String cart(Model model) {

        Member member = memberService.getCurrentMember();

        try {
            List<CartItemDto> cartItems = cartService.findCartItems(member);
            model.addAttribute("cartItems", cartItems);

            return "cart/cart";
        } catch (CartNotFoundException e) {
            return "cart/empty_cart";
        }
    }

    @PostMapping("/cart/add")
    @ApiOperation(value = "장바구니에 상품 추가")
    public @ResponseBody String cartAdd(@Validated CartDto cartDto, BindingResult errors) {

        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        cartService.addCart(cartDto.getId(), cartDto.getQuantity());
        return "success";
    }

    @PostMapping("/cart/update/quantity")
    @ApiOperation(value = "장바구니 상품 수량 변경")
    public @ResponseBody String updateQuantity(@Validated CartDto cartDto, BindingResult errors) {

        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        cartService.updateQuantity(cartDto.getId(), cartDto.getQuantity());
        return "success";
    }

    @DeleteMapping("/cart/delete/item")
    @ApiOperation(value = "장바구니 단일 상품 삭제")
    public @ResponseBody String deleteCartItem(@RequestParam(name = "id") Long id) {

        cartService.deleteCartItem(id);

        return "success";
    }

    @DeleteMapping("cart/delete/itemList")
    @ApiOperation(value = "장바구니 상품 삭제")
    public @ResponseBody String deleteCartItemAll(@RequestParam(required = false) List<Long> itemList) {
        cartService.deleteCartItemAll(itemList);
        return "success";
    }

}

package com.linksang.LinkShop.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linksang.LinkShop.DTO.CartItemDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Cart;
import com.linksang.LinkShop.entity.CartItem;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.linksang.LinkShop.exception.CartNotFoundException;
import com.linksang.LinkShop.exception.ItemNotFoundException;
import com.linksang.LinkShop.exception.MemberNotFoundException;
import com.linksang.LinkShop.repository.CartItemRepository;
import com.linksang.LinkShop.repository.CartRepository;
import com.linksang.LinkShop.repository.ItemRepository;
import com.linksang.LinkShop.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @Override
    public void createCart(Member member) {
        Cart cart = new Cart();
        cart.createCart(member);

    }

    @Override
    @Transactional
    public void addCart(Long id, int quantity) {

        //사용자 아이디
        String userId = security.getName();

        Member member = memberRepository.findByUserId(userId).orElseThrow(
                () -> new MemberNotFoundException("해당 유저는 존재하지 않습니다."));
        Item item = itemRepository.findById(id).orElseThrow(
                () -> new ItemNotFoundException("해당 상품은 존재하지 않습니다"));
        Cart cart = cartRepository.findByMember(member).orElseThrow(
                () -> new CartNotFoundException("존재하지 않는 장바구니입니다."));
        CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);

        //장바구니에 상품이 존재하지 않으면 추가하고 있으면 수량 추가
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, quantity);
        } else {
            cartItem.addQuantity(quantity);
        }

        cartItem.setTotalPrice(cartItem.getItem().getPrice() * cartItem.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public Cart save(Cart cart) {return cartRepository.save(cart);}

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDto> findCartItems(Member member) {

        Cart cart = cartRepository.findByMember(member).orElseThrow(
                () -> new CartNotFoundException("존재하지 않는 장바구니입니다."));

        List<CartItemDto> cartItemList = cartItemRepository.findByCartId(cart.getId()).stream()
                .map(i -> mapper.map(i, CartItemDto.class))
                .collect(Collectors.toList());

        return cartItemList;
    }

    @Override
    public List<OrderItemDto> cartItemToPayment(String cartIdList, HttpSession session) {

        JsonElement jsonElement = JsonParser.parseString(cartIdList);
        JsonArray jsonElements = jsonElement.getAsJsonArray();

        List<Long> cartItemIdList = new ArrayList<>();
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();

        for (int i = 0; i < jsonElements.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonElements.get(i);
            Long cartItemId = jsonObject.get("cartItemId").getAsLong();

            cartItemIdList.add(cartItemId);
        }
        session.setAttribute("cartItemIdList", cartItemIdList);

        for (int i = 0; i < cartItemIdList.size(); i++) {
            CartItem cartItem = cartItemRepository.findById(cartItemIdList.get(i)).orElseThrow(
                    () -> new ItemNotFoundException("해당 장바구니 상품이 존재하지 않습니다."));

            OrderItemDto orderItemDto = OrderItemDto.builder()
                    .item(cartItem.getItem())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getTotalPrice())
                    .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                    .build();
            orderItemDtoList.add(orderItemDto);
        }
        return orderItemDtoList;
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void deleteCartItemAll(List<Long> id) {
        cartItemRepository.deleteAllById(id);
    }

    @Override
    public void updateQuantity(Long id, int quanntity) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(()-> new CartNotFoundException("존재하지 않는 장바구니 상품입니다."));
        cartItem.setQuantity(quanntity);
        cartItem.setTotalPrice(cartItem.getItem().getPrice() + quanntity);
    }
}

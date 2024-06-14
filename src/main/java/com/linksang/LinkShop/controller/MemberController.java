package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.repository.ItemQnARepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MessageService messageService;
    private final RedisService redisService;
    private final ItemQnAService itemQnAService;
    private final ItemQnAReplyService itemQnAReplyService;
    private final CartService cartService;
    private final OrderService orderService;
    private final CommonService commonService;
    private final ItemQnARepository itemQnARepository;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;

    @GetMapping("/join")
    @ApiOperation(value = "회원가입 페이지")
    public String join() {
        return "member/member_join";
    }

    @RequestMapping("/login")
    @ApiOperation(value = "로그인 페이지")
    public String login(HttpServletRequest request, Model model) {

        String userId = (String) request.getAttribute("userId");
        String password = (String) request.getAttribute("password");
        String failMessage = (String) request.getAttribute("LoginFailureMessage");

        model.addAttribute("userId", userId);
        model.addAttribute("password", password);
        model.addAttribute("LoginFailureMessage", failMessage);

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "member/member_login";
    }
}

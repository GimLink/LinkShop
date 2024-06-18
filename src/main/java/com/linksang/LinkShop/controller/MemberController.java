package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.DTO.ItemQnAReplyDto;
import com.linksang.LinkShop.DTO.OrderDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.enums.Sns;
import com.linksang.LinkShop.repository.ItemQnARepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/member/findId")
    @ApiOperation(value = "아이디 찾기 페이지")
    public String findId() {
        return "member/member_findId";
    }

    @GetMapping("/mypage")
    @ApiOperation(value = "마이페이지")
    public String mypage(Model model) {

        Member member = memberService.getCurrentMember();
        List<OrderDto> orderList = orderService.searchAllByMember(null, member);
        Long lastOrderId = orderService.getLastOrderId(orderList, null);

        model.addAttribute("orderList", orderList);
        model.addAttribute("lastOrderId", lastOrderId);

        return "member/member_mypage";
    }

    @GetMapping("/member/withdrawal")
    @ApiOperation(value = "회원탈퇴 페이지")
    public String withdrawal() {
        return "member/member_withdrawal";
    }

    @GetMapping("/member/withdrawal/finalCheck")
    @ApiOperation(value = "회원탈퇴 마지막 비밀번호 검사 페이지")
    public String withdrawalFinalCheck() {
        return "member/member_withdrawalPswd";
    }

    @DeleteMapping("/member/withdrawal")
    @ApiOperation(value = "비밀번호 확인 후 회원탈퇴 처리")
    public @ResponseBody String withdrawalPost(HttpSession session, @RequestParam(name = "password") String password) {

        Member member = memberService.getCurrentMember();

        if (!member.getSns().equals(Sns.NONE)) {
            memberService.withdrawal(member.getUserId());
            memberService.saveWithdrawalMember(member.getUserId());
            session.invalidate();
            return "success";
        }

        boolean result = encoder.matches(password, member.getPassword());
        if (!result) {
            return "fail";
        }

        memberService.withdrawal(member.getUserId());
        memberService.saveWithdrawalMember(member.getUserId());
        session.invalidate();
        return "success";
    }

    @GetMapping("/member/orderDetails/{id}")
    @ApiOperation(value = "주문정보 페이지")
    public String memberOrderDetails(@PathVariable(name = "id") Long orderId, Model model) {
        Order order = orderService.findById(orderId);

        model.addAttribute(orderService.getModelPayInfo(order, model));
        return "member/member_orderDetails";
    }

    @GetMapping("/mypage/orderList")
    @ApiOperation(value = "마이페이지에 주문상품 html 반환", notes = "ajax 전용")
    public String orderListMore(@RequestParam(required = false) Long lastOrderId,
                                @RequestParam(required = false) String more, Model model) {

        Member member = memberService.getCurrentMember();
        List<OrderDto> orderList = orderService.searchAllByMember(lastOrderId, member);

        lastOrderId = orderService.getLastOrderId(orderList, lastOrderId);

        model.addAttribute("orderList", orderList);
        model.addAttribute("lastOrderId", lastOrderId);

        if (more != null) {
            return "member/tab/tab1ordermore";
        }
        return "member/tab/tab1orderList";

    }

    @GetMapping("/mypage/qnaList")
    @ApiOperation(value = "마이페이지에 해당 사용자가 작성한 qna와 답변 담긴 html 반환", notes = "ajax 전용")
    public String qnaList(Model model, @RequestParam(required = false) Long lastQnAId,
                          @RequestParam(required = false) String more) {

        Member member = memberService.getCurrentMember();
        boolean result = itemQnARepository.existsByMember(member);

        Pageable pageable = PageRequest.ofSize(3);
        List<ItemQnADto> qnaList = (!result) ? new ArrayList<>() : itemQnAService.searchAllByMember(lastQnAId, member, pageable);
        List<ItemQnAReplyDto> replyList = itemQnAReplyService.findAllByQnA(qnaList);

        qnaList = itemQnAService.edit(qnaList);
        replyList = itemQnAReplyService.edit(replyList);
        lastQnAId = itemQnAService.getLastQnAId(qnaList, lastQnAId);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("replyList", replyList);
        model.addAttribute("lastQnAId", lastQnAId);

        if (more != null && more.equals("more")) {
            return "member/tab/tab2qnamore";
        }
        return "member/tab/tab2qnaList";
    }


}

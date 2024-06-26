package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.*;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.enums.Sns;
import com.linksang.LinkShop.repository.ItemQnARepository;
import com.linksang.LinkShop.service.*;
import com.linksang.LinkShop.util.CommonService;
import com.linksang.LinkShop.util.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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

//    @RequestMapping("/login")
    @ApiOperation(value = "로그인 페이지")
    public String login(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("prevPage", referer);

        return "member/member_login";
    }

    @RequestMapping("/login")
    @ApiOperation(value = "로그인 페이지")
    public String login(HttpServletRequest request, Model model) {

        String userId = (String) request.getAttribute("userId");
        String password = (String) request.getAttribute("password");
        String failMessage = (String) request.getAttribute("LoginFailureMessage");

        log.info("fail message is " + failMessage);
        log.info("userId is " + userId);

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

    @DeleteMapping("/mypage/qna/delete")
    @ApiOperation(value = "qna 삭제")
    public @ResponseBody String qnaDelete(@RequestParam List<Long> qnaIdList) {

        itemQnAService.deleteAllById(qnaIdList);

        return "success";
    }

    @GetMapping("/member/idConfirm")
    @ApiOperation(value = "아이디 중복검사")
    public @ResponseBody String idConfirm(@RequestParam(name = "userId") String userId) {
        boolean member = memberService.existsByUserId(userId);
        boolean withdrawalMember = memberService.existsWithdrawalByUserId(userId);

        if (!member && !withdrawalMember) return "Y";
        else return "N";
    }

    @GetMapping("/member/sendAuth")
    @ApiOperation(value = "인증번호 전송")
    public @ResponseBody String sendAuth(@RequestParam(name = "phoneNum") String phoneNum) throws Exception {

        boolean result = messageService.phoneValidationCheck(phoneNum);
        int randomNum = messageService.randomNum();

        if (!result) return "N";

        redisService.setAuthNo(phoneNum, randomNum);
        messageService.sendMessage(phoneNum, randomNum);
        return "Y";
    }

    @GetMapping("/member/authNumCheck")
    @ApiOperation(value = "회원가입 인증번호 검사")
    public @ResponseBody String authNumCheck(@RequestParam(value = "authNum") String authNum,
                                             @RequestParam(value = "phoneNum") String phoneNum) {

        int result = redisService.authNumCheck(phoneNum, authNum);


        switch (result) {
            case 1 -> {
                return "Y";
            }
            case 2 -> {
                return "N";
            }
            case 3 -> {
                return "cnt";
            }
            default -> {
                break;
            }
        }
        return "cnt";
    }

    @PostMapping("/join")
    @ApiOperation(value = "일반 회원가입")
    public String join(@ModelAttribute("member") @Validated(ValidationSequence.class) JoinMemberDto joinMemberDto,
                       BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            Map<String, String> errorsMessageMap = memberService.getErrorMsg(errors);
            for (String key : errorsMessageMap.keySet()) {
                model.addAttribute(key, errorsMessageMap.get(key));
            }
            return "member/member_join";
        }
        int checkResult = memberService.joinValidationCheck(joinMemberDto);
        if (checkResult == -1) {
            model.addAttribute(memberService.createJoinDtoErrorMsg(joinMemberDto, model));
            return "member/member_join";
        }

        Member member = mapper.map(joinMemberDto, Member.class);
        cartService.createCart(member);
        memberService.joinNormal(member);

        return "redirect:/";

    }
}

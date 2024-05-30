package com.linksang.LinkShop.service;


import com.linksang.LinkShop.DTO.JoinMemberDto;
import com.linksang.LinkShop.DTO.MemberDto;
import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.WithdrawalMember;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.enums.Sns;
import com.linksang.LinkShop.exception.MemberNotFoundException;
import com.linksang.LinkShop.exception.NotLoginException;
import com.linksang.LinkShop.repository.MemberRepository;
import com.linksang.LinkShop.repository.WithdrawalMemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final CustomUserDetailService customUserDetailService;
    private final WithdrawalMemberRepository withdrawalMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final OrderItemService orderItemService;
    private final SecurityService security;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existItem(Item item, String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
        List<OrderItemDto> orderItemList = orderItemService.searchAllByMember(member);

        return orderItemList.stream()
                .anyMatch(i -> i.getItem().getId().equals(item.getId()));
    }

    @Override
    public boolean checkAuthNum(String phoneNum, String authNum) {
        try {
            int paramAuthNum = Integer.parseInt(authNum);
            int findAuthNum = redisService.getAuthNum(phoneNum);

            return paramAuthNum == findAuthNum;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void setAuthCheck(String phoneNum) throws Exception {
        redisService.setAuthCheck(phoneNum);
    }

    @Override
    @Transactional
    public void saveWithdrawalMember(String userId) {
        WithdrawalMember member = WithdrawalMember.builder()
                .userId(userId)
                .build();

        withdrawalMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void withdrawal(String userId) {
        memberRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllByPhoneNum(String phoneNum) {
        List<String> userIdList = new ArrayList<>();
        List<Member> memberList = memberRepository.findAllByPhoneNum(phoneNum);

        for (Member member : memberList) {
            userIdList.add(member.getUserId());
        }

        return userIdList;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserId(String userId) {
        return memberRepository.existsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsWithdrawalByUserId(String userId) {
        return withdrawalMemberRepository.existByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Member findByUserId(String userId) {

        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디는 존재하지 않습니다." + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원 번호는 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Member getCurrentMember() {

        if (!security.isAuthenticated()) {
            throw new NotLoginException("getCurrentMember, 로그인해주세요.");
        }
        return memberRepository.findByUserId(security.getName())
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디는 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public void login(String userId) {

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDto> findAll(Pageable pageable) {
        //최근 가입한 회원 불러오기

        return memberRepository.findAll(pageable).stream()
                .map(i -> mapper.map(i, MemberDto.class))
                .sorted(Comparator.comparingLong(MemberDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Member joinNormal(Member member) {

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.USER);
        member.setSns(Sns.NONE);

        memberRepository.save(member);
        return member;
    }

    @Override
    @Transactional
    public Member joinOAuth(String id, Sns sns) {

        String password = UUID.randomUUID().toString();
        password = passwordEncoder.encode(password);

        MemberDto memberDto = MemberDto.builder()
                .userId(id)
                .password(password)
                .role(Role.USER)
                .sns(sns)
                .build();

        Member member = mapper.map(memberDto, Member.class);
        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public int joinValidationCheck(JoinMemberDto dto) {
        //회원가입시 validator로 검증하지 못한 부분 유효성 검사

        boolean findMember = memberRepository.existsByUserId(dto.getUserId());
        boolean findWithdrawalMember = withdrawalMemberRepository.existByUserId(dto.getUserId());

        int authNum = redisService.getAuthNum(dto.getPhoneNum());
        int authNumCheckResult = redisService.authNumCheck(dto.getPhoneNum(), dto.getAuthNum());

        if (findMember || findWithdrawalMember || !dto.getPassword().equals(dto.getPswdCheck())
                || authNum == 1 || authNumCheckResult != 1) {
            log.warn("MemberService : join validation check failed");
            return -1;
        }

        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Model createJoinDtoErrorMsg(JoinMemberDto dto, Model model) {
        //회원가입 에러메시지 담기

        String pswd1 = dto.getPassword();
        String pswd2 = dto.getPswdCheck();
        String phoneNum = dto.getPhoneNum();
        String dtoAuthNum = dto.getAuthNum();

        int authNumResult = redisService.authNumCheck(phoneNum, dtoAuthNum);
        boolean findMember = memberRepository.existsByUserId(dto.getUserId());
        boolean findWithdrawalMember = withdrawalMemberRepository.existByUserId(dto.getUserId());

        if (findMember || findWithdrawalMember) {
            model.addAttribute("valid_userId", "이미 사용중이거나 탈퇴한 아이디입니다.");
        }
        if (!pswd1.equals(pswd2)) {
            model.addAttribute("valid_pswdCheck", "비밀번호가 일치하지 않습니다.");
        }
        if (authNumResult == 3) {
            model.addAttribute("valid_authNum", "인증번호를 다시 확인해주세요.");
        } else if (authNumResult == 2) {
            model.addAttribute("valid_authNum", "인증을 다시 진행해주세요");
        }

        return model;
    }

    @Override
    public Map<String, String> getErrorMsg(Errors errors) {

        Map<String, String> map = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            if (map.get("valid_" + error.getField()) != null) {
                continue;
            }

            String key = String.format("valid_%s", error.getField());
            map.put(key, error.getDefaultMessage());
        }
        return map;
    }
}

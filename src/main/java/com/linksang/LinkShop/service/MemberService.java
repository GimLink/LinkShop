package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.JoinMemberDto;
import com.linksang.LinkShop.DTO.MemberDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Sns;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Model createJoinDtoErrorMsg(JoinMemberDto dto, Model model);

    Map<String, String> getErrorMsg(Errors errors);

    int joinValidationCheck(JoinMemberDto dto);

    boolean existItem(Item item, String userId);

    boolean existsByUserId(String userId);

    boolean existsWithdrawalByUserId(String userId);

    boolean checkAuthNum(String phoneNum, String authNum);

    void setAuthCheck(String phoneNum) throws Exception;

    void saveWithdrawalMember(String userId);

    void login(String userId);

    void withdrawal(String userId);

    Member joinNormal(Member member);

    Member joinOAuth(String id, Sns sns);

    Member findByUserId(String userId);

    Member findById(Long id);

    Member getCurrentMember();

    List<MemberDto> findAll(Pageable pageable);

    List<String> findAllByPhoneNum(String phoneNum);

}

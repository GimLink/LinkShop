package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.WithdrawalMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalMemberRepository extends JpaRepository<WithdrawalMember, Long> {

    boolean existsByUserId(String userId);
}

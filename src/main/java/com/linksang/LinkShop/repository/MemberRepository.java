package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);

    List<Member> findAllByPhoneNum(String phoneNum);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}

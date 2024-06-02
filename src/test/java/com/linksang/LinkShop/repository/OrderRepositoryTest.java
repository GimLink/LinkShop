package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Order;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.enums.Sns;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/aws.yml" +
        ",classpath:/redis.yml")
@Transactional
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("특정 회원이 주문한 주문내용 검색")
    void searchAllByMember() {

        //given
        Member member = new Member();
        member.setUserId("link");
        member.setPassword("pswd");
        member.setSns(Sns.NONE);
        member.setRole(Role.USER);

        member = memberRepository.save(member);

        for (int i = 1; i <= 30; i++) {
            orderRepository.save(Order.builder()
                    .member(member)
                    .orderNum("testorderNum" + i)
                    .createdDate(LocalDateTime.now())
                    .build());
        }

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        List<Order> orderList = orderRepository.searchAllByMember(null, findMember);

        //then
        assertAll(
                () -> assertTrue(!orderList.isEmpty()),
                () -> assertTrue(orderList.get(0).getMember().equals(findMember))
        );

    }
}

package com.linksang.LinkShop.repository;


import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.enums.Sns;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.config.location=" +
        "classpath:/application.yml" +
        ",classpath:/aws.yml" +
        ",classpath:/redis.yml")

@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveMember() {

        //given
        Member member = new Member();
        member.setUserId("test");
        member.setPassword("pswd");
        member.setPhoneNum("01000000000");
        member.setSns(Sns.NONE);
        member.setRole(Role.USER);

        //when
        Long id = memberRepository.save(member).getId();

        //then
        Member findMember = memberRepository.findById(id).get();

        Assertions.assertThat(findMember).isEqualTo(member);
    }
}

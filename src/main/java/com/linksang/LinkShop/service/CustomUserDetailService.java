package com.linksang.LinkShop.service;

import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다. 회원 아이디 : " + userId));

        Collection<SimpleGrantedAuthority> role = new ArrayList<SimpleGrantedAuthority>();
        role.add(new SimpleGrantedAuthority(member.getRole().name()));

        return new User(member.getUserId(), member.getPassword(), role);
    }
}

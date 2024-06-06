package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.ItemQnA;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.repository.custom.ItemQnARepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemQnARepository extends JpaRepository<ItemQnA, Long>, ItemQnARepositoryCustom {

    Long countByItem(Item item);

    boolean existsByMember(Member member);
}

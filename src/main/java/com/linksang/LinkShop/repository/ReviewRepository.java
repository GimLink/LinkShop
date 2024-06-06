package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.Review;
import com.linksang.LinkShop.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Long countByItem(Item item);

    boolean existsByItemAndMember(Item item, Member member);
}

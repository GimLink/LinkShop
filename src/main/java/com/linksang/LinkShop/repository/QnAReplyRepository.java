package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.ItemQnAReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnAReplyRepository extends JpaRepository<ItemQnAReply, Long> {

    Optional<ItemQnAReply> findByItemQnAId(Long qnaId);
}

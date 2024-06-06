package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.BoardReComment;
import com.linksang.LinkShop.repository.custom.BoardReCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReCommentRepository extends JpaRepository<BoardReComment, Long>, BoardReCommentRepositoryCustom {
}

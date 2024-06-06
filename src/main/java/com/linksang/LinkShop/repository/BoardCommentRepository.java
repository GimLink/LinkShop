package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;
import com.linksang.LinkShop.repository.custom.BoardCommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentRepositoryCustom {

    Long countByBoard(Board board);
}

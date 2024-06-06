package com.linksang.LinkShop.repository;

import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.repository.custom.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}

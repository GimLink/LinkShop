package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.entity.Board;

import java.util.List;

public interface BoardCommentRepositoryCustom {

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId);
}

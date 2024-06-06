package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.BoardReCommentDto;

import java.util.List;

public interface BoardReCommentRepositoryCustom {

    List<BoardReCommentDto> searchAll(BoardCommentDto comment);
}

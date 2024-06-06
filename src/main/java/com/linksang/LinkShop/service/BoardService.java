package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardDto;
import com.linksang.LinkShop.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    Long count();

    List<BoardDto> searchAll(Pageable pageable);

    Board findById(Long boardId);

    Long save(BoardDto boardDto, String userId);

    Long update(Long boardId, BoardDto boardDto);

    Long updateHide(Long boardId);

    void deleteById(Long boardId);
}

package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.CommentPostDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;

import java.util.List;

public interface BoardCommentService {

    Long getLastCommentId(List<BoardCommentDto> commentList, Long lastCommentId);

    Long countByBoard(Board board);

    BoardComment findById(Long commentId);

    List<BoardCommentDto> searchAll(Board board, Long lastCommentId);

    Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId);

    void deleteById(Long commentId);

    void updateComment(Long commentId, CommentPostDto postDto);

    List<BoardCommentDto> edit(List<BoardCommentDto> commentList);
}

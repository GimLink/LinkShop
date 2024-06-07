package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.BoardReCommentDto;
import com.linksang.LinkShop.DTO.CommentPostDto;
import com.linksang.LinkShop.entity.BoardReComment;

import java.util.List;

public interface BoardReCommentService {

    BoardReComment findById(Long reCommentId);

    List<BoardReCommentDto> searchAll(List<BoardCommentDto> commentList);

    List<BoardReCommentDto> edit(List<BoardReCommentDto> reCommentList);

    Long save(CommentPostDto postDto, String userId);

    void deleteById(Long reCommentId);

    void updateReComment(Long reCommentId, CommentPostDto postDto);

}

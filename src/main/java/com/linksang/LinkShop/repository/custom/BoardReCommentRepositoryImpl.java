package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.BoardReCommentDto;
import com.linksang.LinkShop.entity.QBoardReComment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BoardReCommentRepositoryImpl implements BoardReCommentRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<BoardReCommentDto> searchAll(BoardCommentDto comment) {
        return queryFactory.select(Projections.fields(BoardReCommentDto.class,
                QBoardReComment.boardReComment.id,
                QBoardReComment.boardReComment.board,
                QBoardReComment.boardReComment.comment,
                QBoardReComment.boardReComment.member,
                QBoardReComment.boardReComment.writer,
                QBoardReComment.boardReComment.content,
                QBoardReComment.boardReComment.hide,
                QBoardReComment.boardReComment.createdDate,
                QBoardReComment.boardReComment.modifiedDate))
                .from(QBoardReComment.boardReComment)
                .where(QBoardReComment.boardReComment.comment.id.eq(comment.getId()))
                .orderBy(QBoardReComment.boardReComment.id.asc())
                .fetch();
    }
}

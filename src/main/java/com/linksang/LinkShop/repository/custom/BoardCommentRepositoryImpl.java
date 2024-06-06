package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.QBoardComment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BoardCommentRepositoryImpl implements BoardCommentRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<BoardCommentDto> searchAll(Board board, Long lastCommentId) {
        return queryFactory.select(Projections.fields(BoardCommentDto.class,
                QBoardComment.boardComment.id,
                QBoardComment.boardComment.member,
                QBoardComment.boardComment.content,
                QBoardComment.boardComment.hide,
                QBoardComment.boardComment.writer,
                QBoardComment.boardComment.createdDate,
                QBoardComment.boardComment.modifiedDate
                ))
                .from(QBoardComment.boardComment)
                .where(QBoardComment.boardComment.board.eq(board),
                        ltCommentId(lastCommentId))
                .orderBy(QBoardComment.boardComment.id.desc())
                .fetch();
    }

    private BooleanExpression ltCommentId(Long lastCommentId) {
        if (lastCommentId == null) {
            return null;
        }
        return QBoardComment.boardComment.id.lt(lastCommentId);
    }
}

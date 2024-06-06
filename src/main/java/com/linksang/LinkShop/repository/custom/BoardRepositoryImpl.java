package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.BoardDto;
import com.linksang.LinkShop.entity.QBoard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<BoardDto> searchAll(Pageable pageable) {
        return queryFactory
                .select(Projections.fields(BoardDto.class,
                QBoard.board.id,
                QBoard.board.member,
                QBoard.board.title,
                QBoard.board.content,
                QBoard.board.writer,
                QBoard.board.hide,
                QBoard.board.createdDate,
                QBoard.board.modifiedDate))
                .from(QBoard.board)
                .offset(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .orderBy(QBoard.board.id.desc())
                .fetch();
    }
}

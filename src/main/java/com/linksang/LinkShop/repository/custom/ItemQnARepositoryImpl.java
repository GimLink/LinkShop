package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemQnADto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.QItemQnA;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ItemQnARepositoryImpl implements ItemQnARepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<ItemQnADto> searchAllByItem(Item item, Pageable pageable) {
        return queryFactory.select(Projections.fields(ItemQnADto.class,
                QItemQnA.itemQnA.id,
                QItemQnA.itemQnA.title,
                QItemQnA.itemQnA.content,
                QItemQnA.itemQnA.writer,
                QItemQnA.itemQnA.member,
                QItemQnA.itemQnA.hide,
                QItemQnA.itemQnA.replyEmpty,
                QItemQnA.itemQnA.createdDate))
                .from(QItemQnA.itemQnA)
                .where(QItemQnA.itemQnA.item.eq(item))
                .orderBy(QItemQnA.itemQnA.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable) {
        return queryFactory.select(Projections.fields(ItemQnADto.class,
                QItemQnA.itemQnA.id,
                QItemQnA.itemQnA.writer,
                QItemQnA.itemQnA.content,
                QItemQnA.itemQnA.hide,
                QItemQnA.itemQnA.replyEmpty,
                QItemQnA.itemQnA.createdDate))
                .from(QItemQnA.itemQnA)
                .where(QItemQnA.itemQnA.member.eq(member),
                        ltQnAId(id))
                .orderBy(QItemQnA.itemQnA.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression ltQnAId(Long id) {
        if (id == null) {
            return null;
        }

        return QItemQnA.itemQnA.id.lt(id);
    }
}

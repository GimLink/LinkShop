package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import java.util.List;

public class ItemRepositoryImpl implements ItemRepositoryCustom{
    
    @Autowired
    private JPAQueryFactory queryFactory;
    
    @Override
    public Long searchTotal(String itemName, String category, String saleStatus) {
        return queryFactory
                .select(QItem.item.id)
                .from(QItem.item)
                .where(equalItemName(itemName),
                        equalCategory(category),
                        equalSaleStatus(saleStatus))
                .fetchCount();
    }

    @Override
    public List<ItemDto> searchAll(SearchDto searchDto, Pageable pageable) {

        List<Long> ids = queryFactory
                .select(QItem.item.id)
                .from(QItem.item)
                .where(equalItemName(searchDto.getItemName()),
                        equalCategory(searchDto.getCategory()),
                        equalSaleStatus(searchDto.getSaleStatus()))
                .orderBy(QItem.item.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        return queryFactory
                .select(Projections.fields(ItemDto.class,
                        QItem.item.id,
                        QItem.item.itemName,
                        QItem.item.category,
                        QItem.item.salesStatus,
                        QItem.item.color,
                        QItem.item.size,
                        QItem.item.price,
                        QItem.item.imageUrl,
                        QItem.item.createdDate
                ))
                .from(QItem.item)
                .where(QItem.item.id.in(ids))
                .orderBy(QItem.item.id.desc())
                .fetch();
    }

    @Override
    public List<ItemDto> searchAllNoOffset(String category, Long id, Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable) {
        return null;
    }

    private BooleanExpression equalItemName(String itemName) {
        if (!StringUtils.hasText(itemName)) {
            return null;
        }

        return QItem.item.itemName.contains(itemName);
    }

    private BooleanExpression equalCategory(String category) {
        if (!StringUtils.hasText(category)) {
            return null;
        }
        return QItem.item.category.eq(category);
    }

    private BooleanExpression equalSaleStatus(String saleStatus) {
        if (!StringUtils.hasText(saleStatus)) {
            return null;
        }

        return QItem.item.salesStatus.eq(saleStatus);
    }

}

package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.QItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;



import java.util.List;

public class ItemRepositoryImpl implements ItemRepositoryCustom{
    
    @Autowired
    private JPAQueryFactory queryFactory;
    
    @Override
    public Long searchTotal(String itemName, String category, String saleStatus) {
        return queryFactory
                .select(QItem.item.id.count())
                .from(QItem.item)
                .where(equalItemName(itemName),
                        equalCategory(category),
                        equalSaleStatus(saleStatus))
                .fetchOne();
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
    public List<ItemDto> searchAllNoOffset(String category, Long itemId, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(ItemDto.class,
                        QItem.item.id,
                        QItem.item.itemName,
                        QItem.item.price,
                        QItem.item.imageUrl
                ))
                .from(QItem.item)
                .where(ltItemId(itemId),
                        equalSaleStatus("onsale"),
                        equalCategory(category))
                .orderBy(QItem.item.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<ItemDto> searchAllBySort(String itemName, String sort, String value, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(ItemDto.class,
                        QItem.item.id,
                        QItem.item.itemName,
                        QItem.item.price,
                        QItem.item.imageUrl))
                .from(QItem.item)
                .where(equalItemName(itemName),
                        equalSort(sort, value))
                .orderBy((OrderSpecifier<?>) orderBySort(sort), QItem.item.id.asc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression equalItemName(String itemName) {
        if (StringUtils.isBlank(itemName)) {
            return null;
        }

        return QItem.item.itemName.contains(itemName);
    }

    private BooleanExpression equalCategory(String category) {
        if (StringUtils.isBlank(category)) {
            return null;
        }
        return QItem.item.category.eq(category);
    }

    private BooleanExpression equalSaleStatus(String saleStatus) {
        if (StringUtils.isBlank(saleStatus)) {
            return null;
        }

        return QItem.item.salesStatus.eq(saleStatus);
    }

    private BooleanExpression ltItemId(Long itemId) {
        if(itemId == null) return null;

        return QItem.item.id.lt(itemId);
    }

    private BooleanExpression equalSort(String sort, String value) {
        if(StringUtils.isBlank(value)) return null;

        if (sort.equals("lowPrice")) {
            return QItem.item.price.gt(Integer.parseInt(value));
        }

        return QItem.item.id.lt(Integer.parseInt(value));
    }

    private Object orderBySort(String sort) {
        String lowPrice = "lowPrice";
        if(lowPrice.equals(sort)) return QItem.item.price.asc();

        return QItem.item.id.desc();
    }

}

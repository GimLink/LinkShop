package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.ItemImageDto;
import com.linksang.LinkShop.entity.Item;
import com.linksang.LinkShop.entity.QItemImage;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemImageRepositoryImpl implements ItemImageRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<ItemImageDto> searchAll(Item item) {
        return queryFactory
                .select(Projections.fields(ItemImageDto.class,
                        QItemImage.itemImage.id,
                        QItemImage.itemImage.imageUrl,
                        QItemImage.itemImage.imageName))
                .from(QItemImage.itemImage)
                .where(equalItemId(item))
                .fetch();
    }

    private BooleanExpression equalItemId(Item item) {
        return QItemImage.itemImage.item.eq(item);
    }

}

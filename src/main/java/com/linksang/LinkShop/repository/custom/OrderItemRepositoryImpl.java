package com.linksang.LinkShop.repository.custom;

import com.linksang.LinkShop.DTO.OrderItemDto;
import com.linksang.LinkShop.DTO.SearchDto;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.entity.QOrder;
import com.linksang.LinkShop.entity.QOrderItem;
import com.linksang.LinkShop.enums.DeliveryStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderItemRepositoryImpl implements OrderitemRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto) {
        return queryFactory
                .select(QOrderItem.orderItem.id.count())
                .from(QOrderItem.orderItem)
                .where(
                        QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus),
                        equalCustomerName(searchDto.getCustomerName()),
                        equalOrderNum(searchDto.getOrderNum()),
                        equalItemName(searchDto.getItemName())

                )
                .fetchOne();
    }

    @Override
    public List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable) {
        return queryFactory
                .select(Projections.fields(OrderItemDto.class,
                        QOrderItem.orderItem.order,
                        QOrderItem.orderItem.item,
                        QOrderItem.orderItem.totalPrice,
                        QOrderItem.orderItem.deliveryStatus))
                .from(QOrderItem.orderItem)
                .where(QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus))
                .orderBy(QOrderItem.orderItem.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<OrderItemDto> searchAll(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(QOrderItem.orderItem.id)
                .from(QOrderItem.orderItem)
                .where(
                        equalDeliveryStatus(deliveryStatus),
                        equalCustomerName(searchDto.getCustomerName()),
                        equalOrderNum(searchDto.getOrderNum()),
                        equalItemName(searchDto.getItemName()),
                        betweenDateOrder(searchDto.getFirstDate(), searchDto.getLastDate())
                )
                .orderBy(QOrderItem.orderItem.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return queryFactory.select(Projections.fields(OrderItemDto.class,
                        QOrderItem.orderItem.id,
                        QOrderItem.orderItem.order,
                        QOrderItem.orderItem.item,
                        QOrderItem.orderItem.totalPrice,
                        QOrderItem.orderItem.deliveryStatus,
                        QOrderItem.orderItem.quantity,
                        QOrderItem.orderItem.wayBillNum))
                .from(QOrderItem.orderItem)
                .where(QOrderItem.orderItem.id.in(ids))
                .orderBy(QOrderItem.orderItem.id.desc())
                .fetch();
    }

    @Override
    public List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto, Pageable pageable) {
        return null;
    }

    @Override
    public List<OrderItemDto> searchAllByMember(Member member) {
        return null;
    }

    private BooleanExpression equalCustomerName(String customerName) {
        if (StringUtils.isBlank(customerName)) {
            return null;
        }

        return QOrderItem.orderItem.order.delivery.deliveryAddress.customerName.contains(customerName);
    }

    private BooleanExpression equalOrderNum(String orderNum) {
        if(StringUtils.isBlank(orderNum)){return null;}
        return QOrderItem.orderItem.order.orderNum.contains(orderNum);
    }

    private BooleanExpression equalItemName(String itemName) {
        if(StringUtils.isBlank(itemName)){return null;}
        return QOrderItem.orderItem.item.itemName.contains(itemName);
    }

    private BooleanExpression equalDeliveryStatus(DeliveryStatus deliveryStatus) {
        if(deliveryStatus.equals(DeliveryStatus.EMPTY)){return null;}
        return QOrderItem.orderItem.deliveryStatus.eq(deliveryStatus);
    }

    private BooleanExpression equalSearchDto(SearchDto searchDto) {
        if (StringUtils.isBlank(searchDto.getKeyType())) return null;

        if (searchDto.getKeyType().equals("orderNum")) {
            return QOrderItem.orderItem.order.orderNum.contains(searchDto.getKeyValue());
        } else {
            return QOrderItem.orderItem.item.itemName.contains(searchDto.getKeyValue());

        }
    }

    private BooleanExpression betweenDateOrder(String firstDate, String lastDate) {
        if (StringUtils.isBlank(firstDate) && StringUtils.isBlank(lastDate)) {

            LocalDateTime start = LocalDateTime.now().minusYears(5L);
            LocalDateTime end = LocalDateTime.now();
            return QOrderItem.orderItem.order.createdDate.between(start, end);

        } else if (StringUtils.isNotBlank(firstDate) && StringUtils.isNotBlank(lastDate)) {

            LocalDateTime start = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            LocalDateTime end = LocalDateTime.now();
            return QOrderItem.orderItem.order.createdDate.between(start, end);

        } else if (StringUtils.isBlank(firstDate) && StringUtils.isNotBlank(lastDate)) {

            LocalDateTime start = LocalDateTime.parse(LocalDateTime.now().minusYears(5L).toString());
            LocalDateTime end = LocalDate.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            return QOrderItem.orderItem.order.createdDate.between(start, end);

        } else {

            LocalDateTime start = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            LocalDateTime end = LocalDate.parse(lastDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            return QOrderItem.orderItem.order.createdDate.between(start, end);
        }
    }

}

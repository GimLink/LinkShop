package com.linksang.LinkShop.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "item", indexes = @Index(name = "idx_item", columnList = "itemName, category, salesStatus"))
@Entity
public class Item extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(length = 10)
    private String category;

    @Column(length = 100)
    private String itemName;

    @Column(length = 100)
    private String color;

    @Column(length = 100)
    private String size;

    @Column
    @Min(0)
    private int price;

    @Column(length = 2048)
    private String itemInfo;

    @Column(length = 100)
    private String model;

    @Column(length = 10)
    private String salesStatus;

    @Column
    private String imageUrl;

    @Column
    private String noOffset;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<CartItem> cartItemList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<ItemImage> ItemImageList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<ItemQnA> qnaList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<ItemQnAReply> qnaReplyList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "item")
    private List<Review> reviewList = new ArrayList<>();

    public void setItemImageList(ItemImage itemImage) {
        this.getItemImageList().add(itemImage);
        itemImage.setItem(this);
    }

    public void addQnaList(ItemQnA itemQnA){
        this.getQnaList().add(itemQnA);
        itemQnA.setItem(this);
    }

    public void addReviewList(Review review){
        this.getReviewList().add(review);
        review.setItem(this);
    }

    public void addQnaReplyList(ItemQnAReply qnaReply){
        this.getQnaReplyList().add(qnaReply);
        qnaReply.setItem(this);
    }
}

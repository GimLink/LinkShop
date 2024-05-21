package com.linksang.LinkShop.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Table(name = "item_qna_reply")
@Entity
public class ItemQnAReply extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_qna_reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "qna_id")
    private ItemQnA itemQnA;

    @Column(length = 20, nullable = false)
    private String writer;

    @Column(length = 2048, nullable = false)
    private String content;

    @Column(length = 10)
    public String hide;
}

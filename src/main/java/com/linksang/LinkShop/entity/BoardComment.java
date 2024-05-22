package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Table(name = "board_comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class BoardComment extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String hide;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comment")
    private List<BoardReComment> reCommentList = new ArrayList<>();

    public void addBoardReCommentList(BoardReComment reComment) {
        this.getReCommentList().add(reComment);
        reComment.setComment(this);
    }
}

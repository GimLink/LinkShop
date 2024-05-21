package com.linksang.LinkShop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Board extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 100, nullable = false)
    @Size(max = 100)
    private String title;

    @Column(length = 4096, nullable = false)
    @Size(max = 4096)
    private String content;

    @Column(length = 20, nullable = false)
    @Size(max = 20)
    private String writer;

    @Column(length = 10, nullable = false)
    @Size(max = 10)
    private String hide;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "board")
    private List<BoardComment> boardCommentList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "board")
    private List<BoardReComment> boardReCommentList = new ArrayList<>();

    public void addBoardCommentList(BoardComment comment) {
        this.getBoardCommentList().add(comment);
        comment.setBoard(this);
    }

    public void addBoardReCommentList(BoardReComment reComment) {
        this.getBoardReCommentList().add(reComment);
        reComment.setBoard(this);
    }
}

package com.linksang.LinkShop.entity;

import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.enums.Sns;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private Sns sns;

    @Column(length = 11)
    private String phoneNum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "member")
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<ItemQnA> qnaList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<ItemQnAReply> qnaReplyList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<MemberAddress> memberAddresses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<BoardComment> boardCommentList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<BoardReComment> boardReCommentList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private Set<Review> reviewList = new HashSet<>();

    public void addReviewList(Review review) {
        this.getReviewList().add(review);
        review.setMember(this);
    }

    public void addQnaList(ItemQnA qnaItem) {
        this.getQnaList().add(qnaItem);
        qnaItem.setMember(this);
    }

    public void addQnaReplyList(ItemQnAReply qnaReply){
        this.getQnaReplyList().add(qnaReply);
        qnaReply.setMember(this);
    }

    public void addBoardList(Board board){
        this.getBoardList().add(board);
        board.setMember(this);
    }

    public void addBoardCommentList(BoardComment comment){
        this.getBoardCommentList().add(comment);
        comment.setMember(this);
    }

    public void addBoardReCommentList(BoardReComment reComment){
        this.getBoardReCommentList().add(reComment);
        reComment.setMember(this);
    }

}

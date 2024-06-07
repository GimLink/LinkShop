package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.BoardReCommentDto;
import com.linksang.LinkShop.DTO.CommentPostDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;
import com.linksang.LinkShop.entity.BoardReComment;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.exception.BoardCommentNotFoundException;
import com.linksang.LinkShop.repository.BoardReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardReCommentServiceImpl implements BoardReCommentService{

    private final BoardReCommentRepository boardReCommentRepository;
    private final BoardCommentService boardCommentService;
    private final MemberService memberService;
    private final SecurityService security;

    @Override
    @Transactional(readOnly = true)
    public BoardReComment findById(Long reCommentId) {

        return boardReCommentRepository.findById(reCommentId).orElseThrow(
                () -> new BoardCommentNotFoundException("해당 댓글은 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardReCommentDto> searchAll(List<BoardCommentDto> commentList) {

        List<BoardReCommentDto> reCommentList = new ArrayList<>();
        for (BoardCommentDto comment : commentList) {
            reCommentList.addAll(boardReCommentRepository.searchAll(comment));
        }
        return reCommentList;
    }

    @Override
    @Transactional
    public List<BoardReCommentDto> edit(List<BoardReCommentDto> reCommentList) {

        if (!reCommentList.isEmpty()) {
            if (security.isAuthenticated()) {
                reCommentList.stream()
                        .filter(c -> c.getHide().equals("private"))
                        .filter(c -> !security.getName().equals(c.getComment().getMember().getUserId())) //대댓글 달린 댓글 작성자인지 확인
                        .filter(c -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(c -> !c.getMember().getUserId().equals(security.getName()))
                        .forEach(c -> c.setContent("비밀댓글입니다."));
            } else {
                reCommentList.stream()
                        .filter(c -> c.getHide().equals("private"))
                        .forEach(c -> c.setContent("비밀댓글입니다."));
            }
        }

        return reCommentList;
    }

    @Override
    @Transactional
    public Long save(CommentPostDto postDto, String userId) {

        BoardComment comment = boardCommentService.findById(postDto.getId());
        Member member = memberService.findByUserId(userId);
        Board board = comment.getBoard();

        BoardReComment reComment = BoardReComment.builder()
                .content(postDto.getContent())
                .hide(postDto.getHide())
                .build();

        reComment.setWriter(member.getUserId().substring(0,3) + "***");
        comment.addBoardReCommentList(reComment);
        member.addBoardReCommentList(reComment);
        board.addBoardReCommentList(reComment);


        return boardReCommentRepository.save(reComment).getId();
    }

    @Override
    @Transactional
    public void deleteById(Long reCommentId) {
        boardReCommentRepository.deleteById(reCommentId);
    }

    @Override
    @Transactional
    public void updateReComment(Long reCommentId, CommentPostDto postDto) {
        BoardReComment reComment = boardReCommentRepository.findById(reCommentId).orElseThrow(
                () -> new BoardCommentNotFoundException("해당 댓글은 존재하지 않습니다."));

        reComment.setHide(postDto.getHide());
        reComment.setContent(postDto.getContent());
    }
}

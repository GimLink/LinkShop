package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.CommentPostDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.enums.Role;
import com.linksang.LinkShop.exception.BoardCommentNotFoundException;
import com.linksang.LinkShop.repository.BoardCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService{

    private final BoardCommentRepository boardCommentRepository;
    private final MemberService memberService;
    private final BoardService boardService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();


    @Override
    public Long getLastCommentId(List<BoardCommentDto> commentList, Long lastCommentId) {

        if (commentList.isEmpty()) {
            return lastCommentId;
        } else {
            return commentList.stream().min(Comparator.comparingLong(BoardCommentDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByBoard(Board board) {

        return boardCommentRepository.countByBoard(board);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardComment findById(Long commentId) {

        return boardCommentRepository.findById(commentId).orElseThrow(
                ()-> new BoardCommentNotFoundException("해당 댓글은 존재하지 않습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardCommentDto> searchAll(Board board, Long lastCommentId) {

        return boardCommentRepository.searchAll(board, lastCommentId);
    }

    @Override
    @Transactional
    public Long saveComment(BoardCommentDto boardCommentDto, Long boardId, String userId) {

        Member member = memberService.findByUserId(userId);
        Board board = boardService.findById(boardId);
        BoardComment comment = mapper.map(boardCommentDto, BoardComment.class);

        comment.setWriter(member.getUserId().substring(0, 3) + "***");
        member.addBoardCommentList(comment);
        board.addBoardCommentList(comment);


        return boardCommentRepository.save(comment).getId();
    }

    @Override
    @Transactional
    public void deleteById(Long commentId) {
        boardCommentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, CommentPostDto postDto) {

        BoardComment comment = boardCommentRepository.findById(commentId).orElseThrow(
                ()-> new BoardCommentNotFoundException("해당 댓글은 존재하지 않습니다."));

        comment.setContent(postDto.getContent());
        comment.setHide(postDto.getHide());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardCommentDto> edit(List<BoardCommentDto> commentList) {

        if (!commentList.isEmpty()) {
            if (security.isAuthenticated()) {
                commentList.stream()
                        .filter(c -> c.getHide().equals("private"))
                        .filter(c -> !security.checkHasRole(Role.ADMIN.getValue()))
                        .filter(c -> !c.getMember().getUserId().equals(security.getName()))
                        .forEach(c -> c.setContent("비밀댓글입니다."));
            }else{
                commentList.stream()
                        .filter(c -> c.getHide().equals("private"))
                        .forEach(c -> c.setContent("비밀댓글입니다."));
            }
        }
        return commentList;
    }
}

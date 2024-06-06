package com.linksang.LinkShop.service;

import com.linksang.LinkShop.DTO.BoardDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.exception.BoardNotFoundException;
import com.linksang.LinkShop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final ModelMapper mapper;


    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return boardRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> searchAll(Pageable pageable) {
        return boardRepository.searchAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("해당 게시물은 존재하지 않습니다.")
        );
    }

    @Override
    @Transactional
    public Long save(BoardDto boardDto, String userId) {
        Member member = memberService.findByUserId(userId);
        Board board = mapper.map(boardDto, Board.class);
        board.setWriter(member.getUserId().substring(0, 3) + "***");
        member.addBoardList(board);

        return boardRepository.save(board).getId();
    }

    @Override
    @Transactional
    public Long update(Long boardId, BoardDto boardDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("해당 게시물은 존재하지 않습니다."));

        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setHide(boardDto.getHide());

        return board.getId();
    }

    @Override
    @Transactional
    public Long updateHide(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("해당 게시물은 존재하지 않습니다."));

        if (board.getHide().equals("private")) {
            board.setHide("public");
        }else{
            board.setHide("private");
        }
        return board.getId();
    }

    @Override
    @Transactional
    public void deleteById(Long boardId) {
        boardRepository.deleteById(boardId);

    }
}

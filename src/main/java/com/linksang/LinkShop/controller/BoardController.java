package com.linksang.LinkShop.controller;

import com.linksang.LinkShop.DTO.BoardCommentDto;
import com.linksang.LinkShop.DTO.BoardDto;
import com.linksang.LinkShop.DTO.BoardReCommentDto;
import com.linksang.LinkShop.DTO.CommentPostDto;
import com.linksang.LinkShop.entity.Board;
import com.linksang.LinkShop.entity.BoardComment;
import com.linksang.LinkShop.entity.BoardReComment;
import com.linksang.LinkShop.entity.Member;
import com.linksang.LinkShop.repository.BoardRepository;
import com.linksang.LinkShop.service.BoardCommentService;
import com.linksang.LinkShop.service.BoardReCommentService;
import com.linksang.LinkShop.service.BoardService;
import com.linksang.LinkShop.service.SecurityService;
import com.linksang.LinkShop.util.CommonService;
import com.linksang.LinkShop.util.PaginationShowSizeTen;
import com.linksang.LinkShop.util.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final BoardCommentService boardCommentService;
    private final BoardReCommentService boardReCommentService;
    private final CommonService commonService;
    private final SecurityService securityService;
    private final ModelMapper mapper;

    @GetMapping("/board/freeBoard")
    @ApiOperation(value = "자유게시판 메인페이지")
    public String freeBoard(@RequestParam(name = "page", defaultValue = "1") int curPage, Model model) {

        Long total = boardService.count();
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<BoardDto> boardList = boardService.searchAll(pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("page", page);
        return "board/board_freeBoard";
    }

    @GetMapping("/board/write")
    @ApiOperation(value = "게시판 글 작성 페이지")
    public String boardWritePage() {
        return "board/board_write";
    }

    @GetMapping("/board/write/{boardId}")
    @ApiOperation(value = "게시글 수정 페이지")
    public ModelAndView boardUpdate(@PathVariable(name = "boardId") Long boardId, Principal principal, ModelAndView modelAndView) {

        Optional<Board> board = boardRepository.findById(boardId);

        if (board.isEmpty() || !securityService.isAuthenticated()) {
            modelAndView.setViewName("error/board_refuse");
        } else if (!board.get().getMember().getUserId().equals(principal.getName())) {
            modelAndView.setViewName("error/board_refuse");
        } else {
            BoardDto boardDto = mapper.map(board.get(), BoardDto.class);
            modelAndView.addObject("board", boardDto);
            modelAndView.setViewName("board/board_update");
        }

        return modelAndView;
    }

    @GetMapping("/board/view/{boardId}")
    @ApiOperation(value = "게시글 상세보기 페이지")
    public String boardDetail(@PathVariable(name = "boardId") Long boardId, Model model, Principal principal) {

        Board board = boardService.findById(boardId);
        Member member = board.getMember();
        BoardDto boardDto = mapper.map(board, BoardDto.class);

        Long totalComment = boardCommentService.countByBoard(board);

        boolean role = false;

        if (securityService.isAuthenticated()) {
            if (member.getUserId().equals(principal.getName())) role = true;
        }

        model.addAttribute("totalComment", totalComment);
        model.addAttribute("board", boardDto);
        model.addAttribute("role", role);

        return "board/board_view";
    }

    @GetMapping("/board/reComment/write/{commentId}")
    @ApiOperation(value = "대댓글 작성 페이지")
    public String reCommentWrite(@PathVariable(name = "commentId") Long commentId, Model model) {

        model.addAttribute("commentId", commentId);

        return "board/board_reCommentWrite";
    }

    @GetMapping("/board/commentList")
    @ApiOperation(value = "게시글 댓글 목록 html 반환", notes = "ajax전용 게시글 상세보기 진입시 자동 호출")
    public String getCommentList(@RequestParam(name = "boardId") Long boardId,
                                 @RequestParam(name = "lastCommentId", required = false) Long lastCommentId,
                                 @RequestParam(name = "more", required = false) String more, Model model) {

        Board board = boardService.findById(boardId);
        Long totalComment = boardCommentService.countByBoard(board);

        List<BoardCommentDto> findCommentList = boardCommentService.searchAll(board, lastCommentId);
        List<BoardReCommentDto> findReCommentList = boardReCommentService.searchAll(findCommentList);
        lastCommentId = boardCommentService.getLastCommentId(findCommentList, lastCommentId);

        List<BoardCommentDto> commentList = boardCommentService.edit(findCommentList);
        List<BoardReCommentDto> reCommentList = boardReCommentService.edit(findReCommentList);

        model.addAttribute("commentList", commentList);
        model.addAttribute("reCommentList", reCommentList);
        model.addAttribute("lastCommentId", lastCommentId);
        model.addAttribute("totalComment", totalComment);

        if (more != null) return "board/tab/board_commentListMore";
        return "board/tab/board_commentList";

    }

    @GetMapping("/board/reComment/{reCommentId}")
    @ApiOperation(value = "대댓글 수정 팝업 페이지", notes = "팝업에 띠울 페이지")
    public String boardReComment(@PathVariable(name = "reCommentId") Long reCommentId, Model model) {

        BoardReComment reComment = boardReCommentService.findById(reCommentId);

        model.addAttribute("reCommentId", reCommentId);
        model.addAttribute("content", reComment.getComment());
        model.addAttribute("hide", reComment.getHide());
        return "board/board_reCommentUpdate";
    }

    @PostMapping("/board/write")
    @ApiOperation(value = "게시글 작성")
    public @ResponseBody String boardWrite(@Validated(ValidationSequence.class) BoardDto boardDto, BindingResult errors, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        boardService.save(boardDto, principal.getName());
        return "success";
    }

    @PostMapping("/board/comment/write")
    @ApiOperation(value = "댓글 작성")
    public @ResponseBody String boardCommentWrite(@Validated(ValidationSequence.class) BoardCommentDto boardCommentDto, BindingResult errors, Long boardId,
                                                  Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        boardCommentService.saveComment(boardCommentDto, boardId, principal.getName());
        return "success";
    }

    @PostMapping("/board/reComment/write")
    @ApiOperation(value = "대댓글 작성")
    public @ResponseBody String boardReCommentWrite(@Validated(ValidationSequence.class) BoardReCommentDto boardReCommentDto, BindingResult errors, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        return "success";
    }

    @PatchMapping("/board/write/{boardId}")
    @ApiOperation(value = "게시글 수정")
    public @ResponseBody String boardUpdate(@PathVariable(name = "boardId") Long boardId, @Validated(ValidationSequence.class) BoardDto boardDto, BindingResult errors, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        Board board = boardService.findById(boardId);
        Member member = board.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardService.update(boardId, boardDto);
        return "success";
    }

    @PatchMapping("/board/comment")
    @ApiOperation(value = "댓글 내용 수정")
    public @ResponseBody String commentUpdate(@Validated(ValidationSequence.class) CommentPostDto postDto, BindingResult errors, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        BoardComment comment = boardCommentService.findById(postDto.getId());
        Member member = comment.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardCommentService.updateComment(comment.getId(), postDto);
        return "success";
    }

    @PatchMapping("/board/reComment")
    @ApiOperation(value = "대댓글 수정")
    public @ResponseBody String reCommentUpdate(@Validated(ValidationSequence.class) CommentPostDto postDto, BindingResult errors, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";
        if (errors.hasErrors()) return commonService.getErrorMessage(errors);

        BoardReComment reComment = boardReCommentService.findById(postDto.getId());
        Member member = reComment.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardReCommentService.updateReComment(reComment.getId(), postDto);
        return "success";
    }

    @PatchMapping("/board/update/hide/{boardId}")
    @ApiOperation(value = "게시글 공개 여부 변경")
    public @ResponseBody String boardHideUpdate(@PathVariable(name = "boardId") Long boardId, Principal principal) {

        if (!securityService.isAuthenticated()) {
            return "login";
        }

        Board board = boardService.findById(boardId);
        Member member = board.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardService.updateHide(boardId);
        return "success";
    }

    @DeleteMapping("/board/delete/{boardId}")
    @ApiOperation(value = "게시글 삭제")
    public @ResponseBody String deleteBoard(@PathVariable(name = "boardId") Long boardId, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";

        Board board = boardService.findById(boardId);
        Member member = board.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardService.deleteById(boardId);
        return "success";
    }

    @DeleteMapping("/board/comment")
    @ApiOperation(value = "댓글 삭제")
    public @ResponseBody String deleteComment(@RequestParam(name = "commentId") Long commentId, Principal principal) {

        if (!securityService.isAuthenticated()) return "login";

        BoardComment comment = boardCommentService.findById(commentId);
        Member member = comment.getMember();
        if (!member.getUserId().equals(principal.getName())) return "role";

        boardCommentService.deleteById(commentId);
        return "success";
    }

    @DeleteMapping("/board/reComment")
    @ApiOperation(value = "대댓글 삭제")
    public @ResponseBody String deleteReComment(@RequestParam(name = "reCommentId") Long reCommentId, Principal principal) {

        if(!securityService.isAuthenticated()) return "login";

        BoardReComment reComment = boardReCommentService.findById(reCommentId);
        Member member = reComment.getMember();
        if(!member.getUserId().equals(principal.getName())) return "role";

        boardReCommentService.deleteById(reCommentId);
        return "success";
    }
}

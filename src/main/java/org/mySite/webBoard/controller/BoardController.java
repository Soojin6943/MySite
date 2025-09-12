package org.mySite.webBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.BoardForm;
import org.mySite.webBoard.CommentForm;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.mySite.webBoard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board/list")
    public String list(Model model) {
        List<Board> boardList = this.boardService.getList();
        model.addAttribute("boardList", boardList);
        return "board_list";
    }

    @GetMapping(value = "/board/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, CommentForm commentForm){
        Board board = this.boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board_detail";
    }

    @GetMapping("/board/create")
    public String create(BoardForm boardForm) {
        return "board_form";
    }

    @PostMapping("board/create")
    public String boardCreate(@Valid BoardForm boardForm,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "board_form";
        }
        this.boardService.create(boardForm.getSubject(), boardForm.getContent());
        return "redirect:/board/list";

    }
}

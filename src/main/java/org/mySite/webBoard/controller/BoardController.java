package org.mySite.webBoard.controller;

import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.mySite.webBoard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}

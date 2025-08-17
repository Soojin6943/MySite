package org.mySite.webBoard.controller;

import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("/board/list")
    public String list(Model model) {
        List<Board> boardList = this.boardRepository.findAll();
        model.addAttribute("boardList", boardList);
        return "board_list";
    }
}

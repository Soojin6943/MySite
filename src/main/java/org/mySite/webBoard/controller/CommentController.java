package org.mySite.webBoard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.CommentForm;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.service.BoardService;
import org.mySite.webBoard.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public String createComment(Model model, @PathVariable("id") Long id, @Valid CommentForm commentForm, BindingResult bindingResult) {
        Board board = this.boardService.getBoard(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("board", board);
            return "board_detail";
        }
        this.commentService.create(board, commentForm.getContent());
        return String.format("redirect:/board/detail/%s", id);
    }
}

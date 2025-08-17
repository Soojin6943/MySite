package org.mySite.webBoard.service;

import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.domain.Comment;
import org.mySite.webBoard.repository.BoardRepository;
import org.mySite.webBoard.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void create(Board board, String content){
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(LocalDateTime.now());
        comment.setBoard(board);
        this.commentRepository.save(comment);
    }
}

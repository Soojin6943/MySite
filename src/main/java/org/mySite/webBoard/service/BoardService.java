package org.mySite.webBoard.service;

import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    // todo : dto 만들어서 리팩토링하기
    // todo : impl 만들어서 분리하기
    private final BoardRepository boardRepository;

    public List<Board> getList() {
        return this.boardRepository.findAll();
    }
}

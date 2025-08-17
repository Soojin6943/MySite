package org.mySite.webBoard.service;

import lombok.RequiredArgsConstructor;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.mySite.DataNotFoundException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    // todo : dto 만들어서 리팩토링하기
    // todo : impl 만들어서 분리하기
    private final BoardRepository boardRepository;

    // 게시판 리스트
    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    // 게시판 상세 페이지
    public Board getBoard(Long id){
        // id로 게시판 조회
        Optional<Board> board = this.boardRepository.findById(id);
        // optional 객체이므로 if~else로 검사
        if (board.isPresent()){
            return board.get();
        } else { // 없으면 예외처리
            throw new DataNotFoundException("board not found");
        }
    }
}

package org.mySite;

import org.junit.jupiter.api.Test;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MySiteApplicationTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void testJpa() {
        Board b1 = new Board();
        b1.setSubject("안녕하세요");
        b1.setContent("jpa 테스트 중입니다");
        b1.setCreateDate(LocalDateTime.now());
        this.boardRepository.save(b1);
    }
}

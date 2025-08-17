package org.mySite;

import org.junit.jupiter.api.Test;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        List<Board> all = this.boardRepository.findAll();
        assertEquals(1, all.size());

        Board b = all.get(0);
        assertEquals("안녕하세요", b.getSubject());

        Optional<Board> oq = this.boardRepository.findById(1L);
        if (oq.isPresent()){
            Board bb = oq.get();
            assertEquals("안녕하세요", bb.getSubject());
        }
    }
}

package org.mySite;

import org.junit.jupiter.api.Test;
import org.mySite.webBoard.domain.Board;
import org.mySite.webBoard.domain.Comment;
import org.mySite.webBoard.repository.BoardRepository;
import org.mySite.webBoard.repository.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testJpa() {
        // jpa
        Board b1 = new Board();
        b1.setSubject("안녕하세요");
        b1.setContent("jpa 테스트 중입니다");
        b1.setCreateDate(LocalDateTime.now());
        this.boardRepository.save(b1);

        // repository
        List<Board> all = this.boardRepository.findAll();
        assertEquals(1, all.size());

        Board b = all.get(0);
        assertEquals("안녕하세요", b.getSubject());

        // findById
        Optional<Board> oq = this.boardRepository.findById(1L);
        if (oq.isPresent()){
            Board bb = oq.get();
            assertEquals("안녕하세요", bb.getSubject());
        }

        // findBySubject
        Board qq = this.boardRepository.findBySubject("안녕하세요");
        assertEquals(1, qq.getId());

        // findBySubjectAndContent
        Board sc = this.boardRepository.findBySubjectAndContent("안녕하세요", "jpa 테스트 중입니다");
        assertEquals(1, sc.getId());

        // like
        List<Board> bList = this.boardRepository.findBySubjectLike("안녕%");
        Board bL = bList.get(0);
        assertEquals("안녕하세요", bL.getSubject());

        // comment test
        Comment c = new Comment();
        c.setContent("안녕하세용가리빵");
        c.setBoard(bL);
        c.setCreateDate(LocalDateTime.now());
        this.commentRepository.save(c);
    }
}

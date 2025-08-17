package org.mySite.webBoard.repository;

import org.mySite.webBoard.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findBySubject(String subject);
    Board findBySubjectAndContent(String subject, String content);
    // 특정 문자열 포함하는 지 데이터 조회
    List<Board> findBySubjectLike(String subject);
}

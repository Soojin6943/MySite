package org.mySite.webBoard.repository;

import org.mySite.webBoard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

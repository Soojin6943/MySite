package org.mySite.webBoard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter  // todo : setter 제거하기
@Entity  // 엔티티로 만들기 위해 애터네이션 적용
public class Board {

    @Id  // 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)  // 열 길이 200으로 설정
    private String subject;

    @Column(columnDefinition = "TEXT")  // columnDefinition은 열 데이터의 유형이나 성격을 정의 (TEXT = 텍스트를 열 데이터로 = 글자 수 제한 없음)
    private String content;

    private LocalDateTime createDate;

    // 게시판에서 댓글 참조
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

}

package org.mySite.webBoard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter  // todo : setter 제거하기
@Entity  // 엔티티로 만들기 위해 애터네이션 적용
public class Board {

    @Id  // 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)  // 열 길이 200으로 설정
    private String subject;

    @Column(columnDefinition = "TEXT")  // columnDefinition은 열 데이터의 유형이나 성격을 정의 (TEXT = 텍스트를 열 데이터로 = 글자 수 제한 없음)
    private String content;

    private LocalDateTime createDate;
}

package org.mySite.userPractice.repository;

import org.mySite.userPractice.domain.UserPractice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPracticeRepository extends JpaRepository<UserPractice, Long> {
    Optional<UserPractice> findByLoginId(String loginId);
    Optional<UserPractice> findById(Long Id);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}

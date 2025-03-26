package org.mySite.user.repository;

import org.mySite.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 1개라도 존재하면 바로 탈출 existsBy~
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}

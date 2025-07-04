package org.mySite.userPractice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mySite.user.domain.UserRole;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPractice {

    // pk
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Id
    private String loginId;

    // password
    private String password;

    // nickname
    private String nickname;

    // role
    private UserRole role;
}

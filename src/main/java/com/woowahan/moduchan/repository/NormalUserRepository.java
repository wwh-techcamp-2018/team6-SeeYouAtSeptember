package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.user.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {
    Optional<NormalUser> findByEmail(String email);
}

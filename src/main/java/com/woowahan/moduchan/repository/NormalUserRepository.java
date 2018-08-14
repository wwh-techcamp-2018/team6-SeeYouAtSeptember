package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.user.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {
}

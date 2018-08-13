package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
}

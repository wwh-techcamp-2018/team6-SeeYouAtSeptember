package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.user.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NormalUserRepository extends JpaRepository<NormalUser, Long> {

    public List<NormalUser> findAllByDeletedFalse();

    public Optional<NormalUser> findByIdAndDeletedFalse(Long id);


}

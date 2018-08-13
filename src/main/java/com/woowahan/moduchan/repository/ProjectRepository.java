package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

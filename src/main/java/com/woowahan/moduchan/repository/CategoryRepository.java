package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

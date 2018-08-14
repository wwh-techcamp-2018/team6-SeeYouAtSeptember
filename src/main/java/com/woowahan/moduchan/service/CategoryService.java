package com.woowahan.moduchan.service;

import com.woowahan.moduchan.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // TODO: 2018. 8. 8. 전체 조회, 개별 조회
}

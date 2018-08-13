package com.woowahan.moduchan.service;

import com.woowahan.moduchan.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    // TODO: 2018. 8. 8. 단일 항목 CRUD, 조건별 조회
}

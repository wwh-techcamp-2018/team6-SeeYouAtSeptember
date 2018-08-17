package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ProjectService {

    // TODO: 2018. 8. 8. 단일 항목 CRUD, 조건별 조회

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO) {
        // TODO: 2018. 8. 15. 유저 찾아서 넣기
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        projectRepository.save(Project.from(projectDTO)
                .addCategory(categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new))
                .addUser((null)))
                .addProducts(projectDTO.getProductList());
    }

    @Transactional
    public void deleteProject(Long pid) {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        // TODO: 2018. 8. 16. 해당 유저인지 확인
        projectRepository.findById(pid).orElseThrow(RuntimeException::new).delete();
    }

    @Transactional
    public Project updateProject(ProjectDTO projectDTO) {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        // TODO: 2018. 8. 16. 해당 유저인지 확인
        return projectRepository.findById(projectDTO.getPid()).orElseThrow(RuntimeException::new)
                .updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new));
    }
}

package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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

    public List<Project> getProjectPage(int pageNo) {
        return projectRepository.findAll(PageRequest.of(pageNo,CategoryService.PAGE_PROJECT_COUNT,
                new Sort(Sort.Direction.DESC, "startAt"))).getContent();
    }

    @Transactional
    public void insertProject(ProjectDTO projectDTO) {
        // TODO: 2018. 8. 15. 유저 찾아서 넣기
        // TODO: 2018. 8. 15. 커스텀 에러 생성
       projectRepository.save(Project.from(projectDTO)
               .addCategory(categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new))
               .addUser((null)))
               .addProducts(projectDTO.getProductList());
    }
}

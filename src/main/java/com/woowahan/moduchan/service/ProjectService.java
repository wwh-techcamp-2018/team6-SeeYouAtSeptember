package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import com.woowahan.moduchan.support.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    // TODO: 2018. 8. 8. 단일 항목 CRUD, 조건별 조회

    private final S3Util s3Util;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NormalUserRepository normalUserRepository;

    public List<ProjectDTO> getProjects() {
        return projectRepository.findAll().stream()
                .map(project -> project.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO, UserDTO writer, MultipartFile multipartFile) throws IOException {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        projectDTO.setThumbnailUrl(s3Util.upload(multipartFile, S3Util.DIR_NAME));
        Project newProject = Project.from(
                projectDTO,
                categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new),
                normalUserRepository.findById(writer.getUid()).orElseThrow(RuntimeException::new));
        projectDTO.getProducts().stream().forEach(productDTO -> newProject.addProduct(productDTO));
        projectRepository.save(newProject);
    }

    @Transactional
    public void deleteProject(Long pid, UserDTO userDTO) {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        Project project = projectRepository.findById(pid).orElseThrow(RuntimeException::new);
        if (!project.isOwner(userDTO)) {
            // TODO: 2018. 8. 19.  커스텀 에러 생성
            throw new RuntimeException();
        }
        project.delete();
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDTO, UserDTO userDTO, MultipartFile multipartFile) throws IOException {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        Project project = projectRepository.findById(projectDTO.getPid()).orElseThrow(RuntimeException::new);
        if (!project.isOwner(userDTO)) {
            throw new RuntimeException();
        }
        if (multipartFile != null) {
            s3Util.removeFileFromS3(project.getFileName());
            projectDTO.setThumbnailUrl(s3Util.upload(multipartFile, S3Util.DIR_NAME));
        }
        // TODO: 2018. 8. 21. product 수정 가능하도록 바꾸기
        return projectRepository.findById(projectDTO.getPid()).orElseThrow(RuntimeException::new)
                .updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new))
                .toDTO();
    }

    public Project getProject(Long pid) {
        // TODO: 2018. 8. 20. 커스텀 에러 생성
        return projectRepository.findById(pid).orElseThrow(RuntimeException::new);
    }
}

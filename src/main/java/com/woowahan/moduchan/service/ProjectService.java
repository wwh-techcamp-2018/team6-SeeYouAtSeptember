package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.CategoryNotFoundException;
import com.woowahan.moduchan.exception.ProjectNotFoundException;
import com.woowahan.moduchan.exception.UnAuthorizedException;
import com.woowahan.moduchan.exception.UserNotFoundException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {
    private final List<Long> recommendationProjectIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
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

    public ProjectDTO getProject(Long pid) {
        return projectRepository.findById(pid)
                .orElseThrow(() -> new ProjectNotFoundException())
                .toDTO();
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO, UserDTO writer) {
        Project newProject = Project.from(
                projectDTO,
                categoryRepository.findById(projectDTO.getCid())
                        .orElseThrow(() -> new CategoryNotFoundException()),
                normalUserRepository.findById(writer.getUid())
                        .orElseThrow(() -> new UserNotFoundException()));
        projectDTO.getProducts().stream().forEach(productDTO -> newProject.addProduct(productDTO));
        projectRepository.save(newProject);
    }

    @Transactional
    public void deleteProject(Long pid, UserDTO userDTO) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ProjectNotFoundException());
        if (!project.isOwner(userDTO)) {
            throw new UnAuthorizedException();
        }
        project.delete();
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDTO, UserDTO userDTO) {
        Project project = projectRepository.findById(projectDTO.getPid())
                .orElseThrow(() -> new ProjectNotFoundException());
        if (!project.isOwner(userDTO)) {
            throw new UnAuthorizedException();
        }
        return project.updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid())
                .orElseThrow(() -> new CategoryNotFoundException()))
                .toDTO();
    }

    public String uploadImage(MultipartFile multipartFile, String previousFileUrl) throws IOException {
        if (!previousFileUrl.isEmpty()) {
            s3Util.removeFileFromS3(S3Util.DIR_NAME + previousFileUrl.substring(previousFileUrl.lastIndexOf("/")));
        }
        return s3Util.upload(multipartFile, S3Util.DIR_NAME);
    }

    public List<ProjectDTO> getOwnedProjects(UserDTO loginUserDTO) {
        return projectRepository.findAllByOwnerId(loginUserDTO.getUid()).stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    public List<ProjectDTO> getTop3ByOrderByCurrentFundRaising() {
        return projectRepository.findTop3ByOrderByCurrentFundRaisingDesc().stream()
                .map(project -> project.toDTO()).collect(Collectors.toList());
    }

    public List<ProjectDTO> getRecommendationProjects() {
        return projectRepository.findByIdIn(recommendationProjectIds).stream()
                .map(project -> project.toDTO()).collect(Collectors.toList());
    }

    public List<ProjectDTO> getTop3ByEndAt() {
        return projectRepository.findTop3ByOrderByEndAtAsc().stream()
                .map(project -> project.toDTO()).collect(Collectors.toList());
    }
}

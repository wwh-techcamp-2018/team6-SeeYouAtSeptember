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
import com.woowahan.moduchan.repository.ProductUserMapRepository;
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
    private final S3Util s3Util;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NormalUserRepository normalUserRepository;
    @Autowired
    private ProductUserMapRepository productUserMapRepository;

    public List<ProjectDTO> getProjects() {
        return projectRepository.findAll().stream()
                .map(project -> project.toDTO())
                .collect(Collectors.toList());
    }

    public ProjectDTO getProject(Long pid) {
        return projectRepository.findById(pid)
                .orElseThrow(() -> new ProjectNotFoundException("pid " + pid))
                .toDTO();
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO, UserDTO writer) {
        Project newProject = Project.from(
                projectDTO,
                categoryRepository.findById(projectDTO.getCid())
                        .orElseThrow(() -> new CategoryNotFoundException("cid: " + projectDTO.getCid())),
                normalUserRepository.findById(writer.getUid())
                        .orElseThrow(() -> new UserNotFoundException("uid: " + writer.getUid())));
        projectDTO.getProducts().stream().forEach(productDTO -> newProject.addProduct(productDTO));
        projectRepository.save(newProject);
    }

    @Transactional
    public void deleteProject(Long pid, UserDTO userDTO) {
        Project project = projectRepository.findById(pid)
                .orElseThrow(() -> new ProjectNotFoundException("pid: " + pid));
        if (!project.isOwner(userDTO)) {
            throw new UnAuthorizedException(String.format("pid: {} tried :{} ",
                    pid, userDTO.getUid()));
        }
        project.delete();
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDTO, UserDTO userDTO) {
        Project project = projectRepository.findById(projectDTO.getPid())
                .orElseThrow(() -> new ProjectNotFoundException("pid: " + projectDTO.getPid()));
        if (!project.isOwner(userDTO)) {
            throw new UnAuthorizedException(String.format("project owner: {} tried :{} ",
                    project.toDTO().getPid(), userDTO.getUid()));
        }
        return project.updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid())
                .orElseThrow(() -> new CategoryNotFoundException("cid: " + projectDTO.getCid())))
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

    public List<ProjectDTO> getSupportingProjects(UserDTO loginUserDTO) {
        return productUserMapRepository.findAllByNormalUserId(loginUserDTO.getUid())
                .stream().map(productUserMap -> productUserMap.getProject()).collect(Collectors.toSet())
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    public List<ProjectDTO> getTop3ByOrderByCurrentFundRaising() {
        return projectRepository.findTop3ByOrderByCurrentFundRaisingDesc().stream()
                .map(project -> project.toDTO()).collect(Collectors.toList());
    }
}

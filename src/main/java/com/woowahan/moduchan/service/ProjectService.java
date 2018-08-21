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

    public List<ProjectDTO> getProjects() {
        return projectRepository.findAll().stream()
                .map(project -> project.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO, UserDTO writer, MultipartFile multipartFile) throws IOException {
        projectDTO.setThumbnailUrl(s3Util.upload(multipartFile, S3Util.DIR_NAME));
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
            throw new UnAuthorizedException("pid: " + pid + " tried uid: " + userDTO.getUid());
        }
        project.delete();
    }

    @Transactional
    public ProjectDTO updateProject(ProjectDTO projectDTO, UserDTO userDTO, MultipartFile multipartFile) throws IOException {
        Project project = projectRepository.findById(projectDTO.getPid())
                .orElseThrow(() -> new ProjectNotFoundException("pid: " + projectDTO.getPid()));
        if (!project.isOwner(userDTO)) {
            throw new UnAuthorizedException("project owner: " + project.toDTO().getPid() + " tried uid: " + userDTO.getUid());
        }
        if (multipartFile != null) {
            s3Util.removeFileFromS3(project.getFileName());
            projectDTO.setThumbnailUrl(uploadImage(multipartFile));
        }
        // TODO: 2018. 8. 21. product 수정 가능하도록 바꾸기
        return project.updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid())
                .orElseThrow(() -> new CategoryNotFoundException("cid: " + projectDTO.getCid())))
                .toDTO();
    }

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        return s3Util.upload(multipartFile, S3Util.DIR_NAME);
    }
}

package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ProjectService {

    // TODO: 2018. 8. 8. 단일 항목 CRUD, 조건별 조회

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    private final S3Util s3Util;

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public void createProject(ProjectDTO projectDTO, UserDTO writer, MultipartFile multipartFile) throws IOException {
        // TODO: 2018. 8. 15. 유저 찾아서 넣기
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        projectDTO.setThumbnailUrl(s3Util.upload(multipartFile, "static")); //setter를 사용해도 될까?
        projectRepository.save(Project.from(projectDTO)
                .addCategory(categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new))
                .addUser(normalUserRepository.findByIdAndDeletedFalse(writer.getId()).orElseThrow(RuntimeException::new))
                .addProducts(projectDTO.getProductList()));
    }

    @Transactional
    public void deleteProject(Long pid, UserDTO user) {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        // TODO: 2018. 8. 16. 해당 유저인지 확인
        Project project = projectRepository.findById(pid).orElseThrow(RuntimeException::new);
        if(!project.isOwner(user)) {
            // TODO: 2018. 8. 19.  커스텀 에러 생성
            throw new RuntimeException();
        }
        s3Util.removeFileFromS3(getFileName(project));
        project.delete();
    }

    @Transactional
    public Project updateProject(ProjectDTO projectDTO, UserDTO user, MultipartFile multipartFile) throws IOException {
        // TODO: 2018. 8. 15. 커스텀 에러 생성
        // TODO: 2018. 8. 16. 해당 유저인지 확인
        Project project = projectRepository.findById(projectDTO.getPid()).orElseThrow(RuntimeException::new);
        if(!project.isOwner(user)) {
            // TODO: 2018. 8. 19.  커스텀 에러 생성
            throw new RuntimeException();
        }
        String fileName = getFileName(project);
        if(fileName != multipartFile.getName()) {
            s3Util.removeFileFromS3(fileName);
            projectDTO.setThumbnailUrl(s3Util.upload(multipartFile, "static"));
        }
        return project.updateProject(projectDTO, categoryRepository.findById(projectDTO.getCid()).orElseThrow(RuntimeException::new));
    }

    private String getFileName(Project project) {
        String fileUrl = project.getThumbnailUrl();
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}

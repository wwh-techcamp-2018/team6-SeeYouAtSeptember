package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.service.ProjectService;
import com.woowahan.moduchan.support.S3Util;
import com.woowahan.moduchan.support.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ApiProjectController {
    // TODO: 2018. 8. 16. 스웨거 정리
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> createProject(HttpSession session, @RequestPart(value = "project") ProjectDTO projectDTO,
                                              @RequestPart("file") MultipartFile multipartFile) throws IOException {
        //TODO 로그인 유저 판별.
        projectService.createProject(projectDTO, (UserDTO)session.getAttribute(SessionUtil.LOGIN_USER),multipartFile); //강제형변환?
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(HttpSession session, @PathVariable("pid") Long pid) {
        //TODO 로그인 유저 판별.
        projectService.deleteProject(pid, (UserDTO)session.getAttribute(SessionUtil.LOGIN_USER));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Project> updateProject(HttpSession session, @RequestBody ProjectDTO projectDTO,
                                                 @RequestPart("fileContent") MultipartFile multipartFile) throws IOException {
        //TODO 로그인 유저 판별.
        Project project = projectService.updateProject(projectDTO,(UserDTO)session.getAttribute(SessionUtil.LOGIN_USER), multipartFile);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

}

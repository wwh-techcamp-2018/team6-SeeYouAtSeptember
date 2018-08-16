package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ApiProjectController {
    // TODO: 2018. 8. 16. 스웨거 정리
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createProject(HttpSession session, @RequestBody ProjectDTO projectDTO) {
        //TODO 로그인 유저 판별.
        projectService.createProject(projectDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(HttpSession session, @PathVariable("pid") Long pid) {
        //TODO 로그인 유저 판별.
        projectService.deleteProject(pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Project> updateProject(HttpSession session, @RequestBody ProjectDTO projectDTO) {
        //TODO 로그인 유저 판별.
        return new ResponseEntity<>(projectService.updateProject(projectDTO), HttpStatus.OK);
    }

}

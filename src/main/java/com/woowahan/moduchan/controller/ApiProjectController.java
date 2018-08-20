package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.service.ProjectService;
import com.woowahan.moduchan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ApiProjectController {
    // TODO: 2018. 8. 16. 스웨거 정리
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    // TODO: 2018. 8. 19.
    @GetMapping("")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    // TODO: 2018. 8. 19.
    @PostMapping("")
    public ResponseEntity<Void> createProject(@RequestBody ProjectDTO projectDTO) {
        // TODO: 2018. 8. 18. need to be logined User
        projectService.createProject(projectDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // TODO: 2018. 8. 19.
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(@PathVariable("pid") Long pid) {
        //TODO 로그인 유저 판별.
        projectService.deleteProject(pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO: 2018. 8. 19.
    @PutMapping("")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO) {
        //TODO 로그인 유저 판별.
        return new ResponseEntity<>(projectService.updateProject(projectDTO), HttpStatus.OK);
    }
}

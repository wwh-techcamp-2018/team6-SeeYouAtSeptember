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

    @PostMapping("/chk")
    public ResponseEntity<Void> createProject(HttpSession session, @RequestBody ProjectDTO projectDTO) {
        projectService.createProject(projectDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/chk/{pid}")
    public ResponseEntity<Void> deleteProject(HttpSession session, @PathVariable("pid") Long pid) {
        projectService.deleteProject(pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/chk")
    public ResponseEntity<Project> updateProject(HttpSession session, @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.updateProject(projectDTO), HttpStatus.OK);
    }

}

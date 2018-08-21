package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.ProjectService;
import com.woowahan.moduchan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ApiProjectController {
    // TODO: 2018. 8. 16. 스웨거 정리
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createProject(@RequestBody ProjectDTO projectDTO,
                                              @ApiIgnore @LoginUser UserDTO loginUserDTO) {
        // TODO: 2018. 8. 20. 프로젝트 생성 시 생성하는 사용자 데이터가 필요합니다.
        projectService.createProject(projectDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long pid,
                                              @ApiIgnore @LoginUser UserDTO loginUserDTO) {
        // TODO: 2018. 8. 20. 프로젝트 삭제 시 프로젝트에 대한 사용자의 권한을 확인해야합니다.
        projectService.deleteProject(pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO,
                                                    @ApiIgnore @LoginUser UserDTO loginUserDTO) {
        // TODO: 2018. 8. 20. 프로젝트 갱신 시 프로젝트에 대한 사용자의 권한을 확인해야합니다.
        return new ResponseEntity<>(projectService.updateProject(projectDTO), HttpStatus.OK);
    }
}

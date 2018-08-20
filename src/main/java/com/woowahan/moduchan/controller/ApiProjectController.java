package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.dto.product.ProductUserMapDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.service.ProductUserMapService;
import com.woowahan.moduchan.service.ProjectService;
import com.woowahan.moduchan.support.SessionUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

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

    @Autowired
    private ProductUserMapService productUserMapService;

    @ApiOperation(value = "프로젝트 전체 조회", notes = "모든 프로젝트의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 생성", notes = "프로젝트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "생성 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PostMapping(value = "", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createProject(@ApiIgnore HttpSession session, @RequestPart(value = "project") ProjectDTO projectDTO,
                                              @RequestPart("file") MultipartFile multipartFile) throws IOException {
        projectService.createProject(projectDTO, (UserDTO) session.getAttribute(SessionUtil.LOGIN_USER), multipartFile);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 403, message = "프로젝트 작성자가 아닌 사용자 접근")
            //error에 대한 설명 추가
    })
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(@ApiIgnore HttpSession session, @PathVariable("pid") Long pid) {
        projectService.deleteProject(pid, (UserDTO) session.getAttribute(SessionUtil.LOGIN_USER));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 정보 갱신", notes = "프로젝트의 정보를 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "갱신 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 403, message = "프로젝트 작성자가 아닌 사용자 접근")
            //error에 대한 설명 추가
    })
    @PutMapping("")
    public ResponseEntity<Project> updateProject(@ApiIgnore HttpSession session, @RequestBody ProjectDTO projectDTO,
                                                 @RequestPart("fileContent") MultipartFile multipartFile) throws IOException {
        Project project = projectService.updateProject(projectDTO, (UserDTO) session.getAttribute(SessionUtil.LOGIN_USER), multipartFile);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @ApiOperation(value = "후원", notes = "상품을 후원합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "후원 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PostMapping("/{pid}/donate")
    public ResponseEntity<Void> donateProject(@ApiIgnore HttpSession session, @RequestBody ProductUserMapDTO productUserMapDTO) {
        productUserMapService.donateProduct((UserDTO) session.getAttribute(SessionUtil.LOGIN_USER), productUserMapDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "후원 취소", notes = "후원을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "취소 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @DeleteMapping("/{pid}/donate")
    public ResponseEntity<Void> cancelDonateProject(@ApiIgnore HttpSession session, @PathVariable("pid") Long pid) {
        productUserMapService.cancelDonateProduct((UserDTO) session.getAttribute(SessionUtil.LOGIN_USER), pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

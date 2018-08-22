package com.woowahan.moduchan.controller;


import com.woowahan.moduchan.dto.product.ProductUserMapDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.ProductUserMapService;
import com.woowahan.moduchan.service.ProjectService;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ApiProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProductUserMapService productUserMapService;

    @ApiOperation(value = "프로젝트 전체 조회", notes = "모든 프로젝트의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @GetMapping("")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        return new ResponseEntity<>(projectService.getProjects(), HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 조회", notes = "프로젝트의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{pid}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long pid) {
        return new ResponseEntity<>(projectService.getProject(pid), HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 생성", notes = "프로젝트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "생성 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @PostMapping(value = "")
    public ResponseEntity<Void> createProject(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody ProjectDTO projectDTO) {
        projectService.createProject(projectDTO, loginUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "프로젝트 삭제", notes = "프로젝트를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 403, message = "프로젝트 작성자가 아닌 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProject(@ApiIgnore @LoginUser UserDTO loginUserDTO, @PathVariable("pid") Long pid) {
        projectService.deleteProject(pid, loginUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "프로젝트 정보 갱신", notes = "프로젝트의 정보를 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "갱신 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 403, message = "프로젝트 작성자가 아닌 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @PutMapping("")
    public ResponseEntity<ProjectDTO> updateProject(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody ProjectDTO projectDTO) {
        return new ResponseEntity<>(projectService.updateProject(projectDTO, loginUserDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "이미지 업로드", notes = "이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "업로드 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestPart("file") MultipartFile multipartFile,
                                                    @RequestParam("previousFileUrl") String previousFileUrl) throws IOException {
        return new ResponseEntity<>(projectService.uploadImage(multipartFile, previousFileUrl), HttpStatus.OK);
    }

    @ApiOperation(value = "후원", notes = "상품을 후원합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "후원 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @PostMapping("/{pid}/donate")
    public ResponseEntity<Void> donateProject(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody ProductUserMapDTO productUserMapDTO) {
        productUserMapService.donateProduct(loginUserDTO, productUserMapDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "후원 취소", notes = "후원을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "취소 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            // TODO: 2018. 8. 21.  error에 대한 설명 추가
    })
    @DeleteMapping("/{pid}/donate")
    public ResponseEntity<Void> cancelDonateProject(@ApiIgnore @LoginUser UserDTO loginUserDTO, @PathVariable("pid") Long pid) {
        productUserMapService.cancelDonateProduct(loginUserDTO, pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

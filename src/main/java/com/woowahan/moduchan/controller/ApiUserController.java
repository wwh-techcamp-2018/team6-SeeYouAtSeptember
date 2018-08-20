package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.exception.UnauthorizedException;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.UserService;
import com.woowahan.moduchan.support.SessionUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;

    // TODO: 2018. 8. 14. getAllUsers: normalUser + adminUser가 필요하지 않을까?
    // TODO: 2018. 8. 15. UserApi가 언제 호출되는지, 호출의 권한에 대하여 논의해볼 필요가 있습니다.

    @ApiOperation(value = "일반 유저 전체 조회", notes = "모든 일반 유저의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getNormalUsers() {
        return new ResponseEntity<>(userService.findAllNomalUser(), HttpStatus.OK);
    }

    @ApiOperation(value = "일반 유저 조회", notes = "특정 일반 유저의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getNormalUser(@PathVariable Long uid) {
        return new ResponseEntity<>(userService.findNormalUserById(uid), HttpStatus.OK);
    }

    @ApiOperation(value = "일반 유저 생성", notes = "일반 유저를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "생성 성공"),
            @ApiResponse(code = 400, message = "유효성 검사 실패")
            //error에 대한 설명 추가
    })
    @PostMapping("")
    public ResponseEntity<Void> createNormalUser(@Validated(UserDTO.JoinValid.class) @RequestBody UserDTO userDTO,
                                                 @ApiIgnore HttpSession session) {
        session.setAttribute(SessionUtil.LOGIN_USER, userService.createNormalUser(userDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "일반 유저 삭제", notes = "일반 유저를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "삭제 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteNormalUser(@PathVariable Long uid, @ApiIgnore @LoginUser UserDTO loginUserDTO) {
        if (loginUserDTO.getId() != uid)
            throw new UnauthorizedException();
        userService.deleteNormalUserById(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "일반 유저 정보 갱신", notes = "일반 유저의 정보를 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "갱신 성공"),
            @ApiResponse(code = 400, message = "유효성 검사 실패"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PutMapping("")
    public ResponseEntity<Void> updateNormalUser(@Validated(UserDTO.JoinValid.class) @RequestBody UserDTO userDTO,
                                                 @ApiIgnore HttpSession session, @ApiIgnore @LoginUser UserDTO loginUserDTO) {
        // FIXME: 2018. 8. 20. session에 user정보 일부만 담을 경우 update함수를 고쳐야함
        loginUserDTO.update(userDTO);
        userService.updateNormalUser(loginUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", notes = "로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 400, message = "유효성 검사 실패")
            //error에 대한 설명 추가
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Validated(UserDTO.LoginValid.class) @RequestBody UserDTO userDTO,
                                         @ApiIgnore HttpSession session) {
        UserDTO loginUserDTO = userService.login(userDTO);
        session.setAttribute(SessionUtil.LOGIN_USER, loginUserDTO);
        return new ResponseEntity<>(loginUserDTO, HttpStatus.OK);
    }
}

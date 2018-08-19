package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.service.UserService;
import com.woowahan.moduchan.support.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;

    // TODO: 2018. 8. 14. getAllUsers: normalUser + adminUser가 필요하지 않을까?
    // TODO: 2018. 8. 15. UserApi가 언제 호출되는지, 호출의 권한에 대하여 논의해볼 필요가 있습니다.

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getNormalUsers() {
        return new ResponseEntity<>(userService.findAllNomalUser(), HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getNormalUser(@PathVariable Long uid) {
        return new ResponseEntity<>(userService.findNormalUserById(uid), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createNormalUser(@Validated(UserDTO.JoinValid.class) @RequestBody UserDTO userDTO, HttpSession session) {
        session.setAttribute(SessionUtil.LOGIN_USER, userService.createNormalUser(userDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/chk/{uid}")
    public ResponseEntity<Void> deleteNormalUser(@PathVariable Long uid) {
        userService.deleteNormalUserById(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/chk")
    public ResponseEntity<Void> updateNormalUser(@Validated(UserDTO.JoinValid.class) @RequestBody UserDTO userDTO, HttpSession session) {
        UserDTO loginUserDTO = (UserDTO) session.getAttribute(SessionUtil.LOGIN_USER);
        loginUserDTO.update(userDTO);
        userService.updateNormalUser(loginUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Validated(UserDTO.LoginValid.class) @RequestBody UserDTO userDTO, HttpSession session) {
        UserDTO loginUserDTO = userService.login(userDTO);
        session.setAttribute(SessionUtil.LOGIN_USER, loginUserDTO);
        return new ResponseEntity<>(loginUserDTO, HttpStatus.OK);
    }
}

package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    @Autowired
    private UserService userService;

    // TODO: 2018. 8. 14. getAllUsers: normalUser + adminUser가 필요하지 않을까?
    // TODO: 2018. 8. 14. create에 들어오는 request body와 update로 들어오는
    // request body에서 내용물이 다른데, @valid가 두 경우 모두 커버할 수 있을지?

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getNormalUsers() {
        return new ResponseEntity<List<UserDTO>>(userService.findAllNomalUser(), HttpStatus.OK);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getNormalUser(@PathVariable Long uid) {
        return new ResponseEntity<UserDTO>(userService.findNormalUserById(uid), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {

        return null;
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long uid) {
        userService.deleteNormalUserById(uid);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
        return null;
    }
}

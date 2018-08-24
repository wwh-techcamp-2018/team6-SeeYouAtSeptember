package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.product.ProductUserMapDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.ProductUserMapService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/products")
public class ApiProductController {

    @Autowired
    private ProductUserMapService productUserMapService;

    @ApiOperation(value = "후원", notes = "상품을 후원합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "후원 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PostMapping("")
    public ResponseEntity<Void> donateProduct(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody ProductUserMapDTO productUserMapDTO) {
        productUserMapService.donateProduct(loginUserDTO, productUserMapDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "후원 취소", notes = "후원을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "취소 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> cancelDonateProduct(@ApiIgnore @LoginUser UserDTO loginUserDTO, @PathVariable Long pid) {
        productUserMapService.cancelDonateProduct(loginUserDTO, pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.OrderHistoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class ApiOrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;


    @ApiOperation(value = "후원 생성", notes = "결제하기 전 후원을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "후원 생성 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PostMapping("")
    public ResponseEntity<OrderHistoryDTO> createOrder(@LoginUser UserDTO loginUser, @RequestBody List<OrderHistoryDTO> orderHistoryDTOList) {
        return new ResponseEntity<>(orderHistoryService.createOrder(orderHistoryDTOList, loginUser), HttpStatus.OK);
    }


    @ApiOperation(value = "후원", notes = "상품을 후원합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "후원 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근")
            //error에 대한 설명 추가
    })
    @PutMapping("")
    public ResponseEntity<Void> donateProduct(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody String oid) {
        orderHistoryService.donateProduct(loginUserDTO, oid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

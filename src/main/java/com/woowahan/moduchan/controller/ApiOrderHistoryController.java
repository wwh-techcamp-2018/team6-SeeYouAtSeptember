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

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class ApiOrderHistoryController {

    @Autowired
    private OrderHistoryService orderHistoryService;


    @ApiOperation(value = "주문 생성", notes = "결제하기 전 주문을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "주문 생성 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 400, message = "잘못된 요청으로 주문 생성 실패")
    })
    @PostMapping("")
    public ResponseEntity<OrderHistoryDTO> createOrder(@LoginUser UserDTO loginUser,@Valid @RequestBody List<OrderHistoryDTO> orderHistoryDTOList) {
        return new ResponseEntity<>(orderHistoryService.createOrder(orderHistoryDTOList, loginUser), HttpStatus.CREATED);
    }


    @ApiOperation(value = "결제 성공 후 주문 내역 성공으로 변경", notes = "상품주문에 성공합니다. 결제 모듈에서 결제성공")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "주문 성공"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 400, message = "잘못된 요청으로 주문 성공처리 실패")
    })
    @PutMapping("")
    public ResponseEntity<Void> donateProduct(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody String oid) {
        orderHistoryService.donateProduct(loginUserDTO, oid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "결제 실패 후 주문 내역 실패로 변경", notes = "상품주문 실패합니다. 결제 모듈에서 결제 실패")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "주문 내역 실패로 변경"),
            @ApiResponse(code = 401, message = "로그인 되지 않은 사용자 접근"),
            @ApiResponse(code = 400, message = "잘못된 요청으로 결제 실패 처리 된 주문내역 실패로 변경 실패")
    })
    @PutMapping("/fail")
    public ResponseEntity<Void> orderFail(@ApiIgnore @LoginUser UserDTO loginUserDTO, @RequestBody String oid) {
        orderHistoryService.orderFail(loginUserDTO, oid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

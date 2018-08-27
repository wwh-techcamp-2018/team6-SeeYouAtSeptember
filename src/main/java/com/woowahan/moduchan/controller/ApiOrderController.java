package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.security.LoginUser;
import com.woowahan.moduchan.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderHistoryDTO> createOrder(@LoginUser UserDTO loginUser, @RequestBody OrderHistoryDTO orderHistoryDTO){
        return new ResponseEntity<>(orderService.createOrder(orderHistoryDTO,loginUser),HttpStatus.OK);
    }

}

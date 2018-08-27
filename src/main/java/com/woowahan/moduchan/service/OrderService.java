package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderHistoryDTO createOrder(OrderHistoryDTO orderHistoryDTO, UserDTO userDTO){
        return orderRepository.save(OrderHistory.from(orderHistoryDTO,userDTO.getUid())).toDTO();
    }
}

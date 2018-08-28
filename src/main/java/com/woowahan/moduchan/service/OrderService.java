package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrderHistoryDTO createOrder(List<OrderHistoryDTO> orderHistoryDTOList, UserDTO userDTO) {
        // TODO: 2018. 8. 28. 유효갯수 이상 구매시 에러발생
        String merchantUid = UUID.randomUUID().toString();

        List<OrderHistory> orderHistories = orderHistoryDTOList.stream().map(orderHistoryDTO -> orderRepository.
                save(OrderHistory.from(orderHistoryDTO, userDTO.getUid(), merchantUid))).collect(Collectors.toList());

        return orderHistories.get(0).toDTO(orderHistories.size(), orderHistories.stream()
                .map(orderHistory -> orderHistory.getPurchasePrice()).reduce(0L, (x, y) -> x + y));
    }
}

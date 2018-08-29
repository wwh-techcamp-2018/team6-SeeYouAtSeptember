package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.order.OrderHistoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.event.ProjectUpdateEventPublisher;
import com.woowahan.moduchan.exception.OrderNotFoundException;
import com.woowahan.moduchan.exception.UserNotFoundException;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.OrderHistoryRepository;
import com.woowahan.moduchan.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private ProjectUpdateEventPublisher projectUpdateEventPublisher;


    @Transactional
    public OrderHistoryDTO createOrder(List<OrderHistoryDTO> orderHistoryDTOList, UserDTO userDTO) {
        String merchantUid = UUID.randomUUID().toString();

        List<OrderHistory> orderHistories = orderHistoryDTOList.stream().map(orderHistoryDTO -> orderHistoryRepository.
                save(OrderHistory.from(orderHistoryDTO, getNormalUser(userDTO), getProduct(orderHistoryDTO), merchantUid)))
                .collect(Collectors.toList());

        return orderHistories.get(0).toDTO(orderHistories.size(), orderHistories.stream()
                .map(orderHistory -> orderHistory.calculateTotalProductPrice()).reduce(0L, (x, y) -> x + y));
    }

    @Transactional
    public void donateProduct(UserDTO loginUserDTO, String oid) {
        List<OrderHistory> orderHistories = getSuccessOrderHistories(oid, loginUserDTO.getUid());

        orderHistories.forEach(orderHistory -> orderHistory.getProject()
                .updateCurrentFundRaising(orderHistory.calculateTotalProductPrice()));

        projectUpdateEventPublisher.publishEvent(orderHistories.get(0).getProject());
    }

    @Transactional
    public void updatePendingOrderHistory() {
        Date limitTime = Date.from(LocalDateTime.now().minusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());

        orderHistoryRepository.findByStatusAndCreatedAtLessThan(OrderHistory.STATUS.PENDING, limitTime)
                .forEach(orderHistory -> orderHistory.updatePendingOrderIntoFail());
    }

    @Transactional
    public void orderFail(UserDTO loginUserDTO, String oid) {
        orderHistoryRepository.findByMerchantUidAndNormalUser(oid,getNormalUser(loginUserDTO))
                .orElseThrow(OrderNotFoundException::new).forEach(orderHistory -> orderHistory.updatePendingOrderIntoFail());
    }

    private Product getProduct(OrderHistoryDTO orderHistoryDTO) {
        return productRepository.findById(orderHistoryDTO.getProductDTO().getPid())
                .orElseThrow(OrderNotFoundException::new).updateRemainQuantity(orderHistoryDTO.getQuantity());
    }

    private NormalUser getNormalUser(UserDTO userDTO) {
        return normalUserRepository.findById(userDTO.getUid()).orElseThrow(UserNotFoundException::new);
    }

    private List<OrderHistory> getSuccessOrderHistories(String oid, Long uid) {
        return orderHistoryRepository.findByMerchantUidAndNormalUser(oid,
                normalUserRepository.findById(uid).orElseThrow(UserNotFoundException::new))
                .orElseThrow(OrderNotFoundException::new)
                .stream().map(orderHistory -> orderHistory.changeOrderStatusSuccess())
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getSupportingProjects(UserDTO loginUserDTO) {
        return orderHistoryRepository.findAllByNormalUser(normalUserRepository.findById(loginUserDTO.getUid()).orElse(null))
                .stream().map(orderHistory -> orderHistory.getProject(OrderHistory.STATUS.SUCCESS)).collect(Collectors.toSet())
                .stream().filter(project -> project != null).map(project -> project.toDTO()).collect(Collectors.toList());
    }
}

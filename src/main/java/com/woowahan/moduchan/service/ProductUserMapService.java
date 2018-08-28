package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.product.ProductUserMap;
import com.woowahan.moduchan.domain.product.ProductUserPK;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.event.ProjectUpdateEventPublisher;
import com.woowahan.moduchan.exception.OrderNotFoundException;
import com.woowahan.moduchan.exception.ProductNotFoundException;
import com.woowahan.moduchan.exception.UserNotFoundException;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.OrderRepository;
import com.woowahan.moduchan.repository.ProductRepository;
import com.woowahan.moduchan.repository.ProductUserMapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ProductUserMapService {
    @Autowired
    private ProductUserMapRepository productUserMapRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private ProjectUpdateEventPublisher projectUpdateEventPublisher;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void donateProduct(UserDTO userDTO, String oid) {
        List<OrderHistory> orderHistories = orderRepository.findByMerchantUidAndUid(oid, userDTO.getUid())
                .orElseThrow(OrderNotFoundException::new)
                .stream().map(orderHistory -> orderHistory.changeOrderStatusSuccess())
                .collect(Collectors.toList());

        List<ProductUserMap> productUserMaps = orderHistories.stream()
                .map(orderHistory -> productUserMapRepository
                        .findById(new ProductUserPK(orderHistory.getPid(), orderHistory.getUid()))
                        .orElse(createProductUserMap(orderHistory))).collect(Collectors.toList());

        IntStream.range(0, productUserMaps.size())
                .forEach(idx -> updateProductUserMap(productUserMaps.get(idx), orderHistories.get(idx)));

        projectUpdateEventPublisher.publishEvent(productUserMaps.get(0).getProject());
    }

    @Transactional
    public void cancelDonateProduct(UserDTO loginUserDTO, Long pid) {
        // TODO: 2018. 8. 19. 해당 productUserMap에 값이 없을 때 custom exception 발생
        productUserMapRepository.findById(new ProductUserPK(loginUserDTO.getUid(), pid))
                .orElseThrow(RuntimeException::new).delete();
    }

    private void updateProductUserMap(ProductUserMap productUserMap, OrderHistory orderHistory) {
        productUserMap.getProject().updateCurrentFundRaising(orderHistory.getPurchasePrice());

        if (productUserMap.isDeleted()) {
            productUserMap.updateQuantity(orderHistory.getQuantity());
            productUserMapRepository.save(productUserMap).getProject();
        }
        productUserMap.appendQuantity(orderHistory.getQuantity());
        productUserMapRepository.save(productUserMap).getProject();
    }

    private ProductUserMap createProductUserMap(OrderHistory orderHistory) {
        Product product = productRepository.findById(orderHistory.getPid())
                .orElseThrow(() -> new ProductNotFoundException("pid: " + orderHistory.getPid()));

        NormalUser normalUser = normalUserRepository.findById(orderHistory.getUid())
                .orElseThrow(() -> new UserNotFoundException("uid: " + orderHistory.getUid()));

        return new ProductUserMap(product, normalUser, 0L, false);
    }
}

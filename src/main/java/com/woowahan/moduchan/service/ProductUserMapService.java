package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.product.ProductUserMap;
import com.woowahan.moduchan.domain.product.ProductUserPK;
import com.woowahan.moduchan.dto.user.UserDTO;
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
    private OrderRepository orderRepository;

    @Transactional
    public void donateProduct(UserDTO userDTO,String oid) {
        // TODO: 2018. 8. 22. 리팩토링!!!!!!!!!!!!!!!
        OrderHistory orderHistory = orderRepository.findByIdAndUid(oid,userDTO.getUid()).orElseThrow(OrderNotFoundException::new).changeOrderStatusSuccess();

        ProductUserMap productUserMap = productUserMapRepository.findById(new ProductUserPK(orderHistory.getPid(), orderHistory.getUid()))
                .orElse(new ProductUserMap(productRepository.findById(orderHistory.getPid())
                        .orElseThrow(() -> new ProductNotFoundException("pid: " + orderHistory.getPid())),
                        normalUserRepository.findById(orderHistory.getUid())
                                .orElseThrow(() -> new UserNotFoundException("uid: " + orderHistory.getUid())),
                        0L, false));

        productUserMap.getProduct().getProject().UpdateCurrentFundRaising(orderHistory.getPurchasePrice());

        if (productUserMap.isDeleted()) {
            productUserMap.updateQuantity(orderHistory.getQuantity());
            productUserMapRepository.save(productUserMap);
        }
        productUserMap.appendQuantity(orderHistory.getQuantity());
        productUserMapRepository.save(productUserMap);
    }


    @Transactional
    public void cancelDonateProduct(UserDTO loginUserDTO, Long pid) {
        // TODO: 2018. 8. 19. 해당 productUserMap에 값이 없을 때 custom exception 발생
        productUserMapRepository.findById(new ProductUserPK(loginUserDTO.getUid(), pid))
                .orElseThrow(RuntimeException::new).delete();
    }
}

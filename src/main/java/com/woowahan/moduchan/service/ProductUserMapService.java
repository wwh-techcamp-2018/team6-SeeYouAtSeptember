package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.product.ProductUserMap;
import com.woowahan.moduchan.domain.product.ProductUserPK;
import com.woowahan.moduchan.dto.product.ProductUserMapDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.ProductNotFoundException;
import com.woowahan.moduchan.exception.UserNotFoundException;
import com.woowahan.moduchan.repository.NormalUserRepository;
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

    public void donateProduct(UserDTO loginUser, ProductUserMapDTO productUserMapDTO) {
        productUserMapRepository.save(
                new ProductUserMap(productRepository.findById(productUserMapDTO.getPid())
                        .orElseThrow(() -> new ProductNotFoundException("pid: " + productUserMapDTO.getPid())),
                        normalUserRepository.findById(productUserMapDTO.getUid())
                                .orElseThrow(() -> new UserNotFoundException("uid: " + productUserMapDTO.getUid()))
                        , productUserMapDTO.getQuantity(), false));
    }

    @Transactional
    public void cancelDonateProduct(UserDTO userDTO, Long pid) {
        // TODO: 2018. 8. 19. 해당 productUserMap에 값이 없을 때 custom exception 발생
        productUserMapRepository.findById(new ProductUserPK(userDTO.getUid(), pid))
                .orElseThrow(RuntimeException::new).delete();
    }
}

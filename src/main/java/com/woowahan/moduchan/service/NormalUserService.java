package com.woowahan.moduchan.service;

import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.repository.ProductUserMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalUserService {
    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private ProductUserMapRepository productUserMapRepository;
}

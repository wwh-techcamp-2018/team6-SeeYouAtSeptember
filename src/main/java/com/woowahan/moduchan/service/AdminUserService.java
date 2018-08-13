package com.woowahan.moduchan.service;

import com.woowahan.moduchan.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepository;

    // TODO: 2018. 8. 8. 프로젝트 심사를 위한 권한체크
}

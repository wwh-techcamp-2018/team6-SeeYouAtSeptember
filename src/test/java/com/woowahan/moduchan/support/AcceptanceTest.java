package com.woowahan.moduchan.support;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class AcceptanceTest {
    private static final String DEFAULT_LOGIN_USER = "a";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private NormalUserRepository normalUserRepository;

    public TestRestTemplate template() {
        return template;
    }

    public TestRestTemplate templateWithNormalUser() {
        return template().withBasicAuth("a", "a");
    }

    protected NormalUser defaultUser() {
        return findByEmail(DEFAULT_LOGIN_USER);
    }

    protected NormalUser findByEmail(String email) {
        return normalUserRepository.findByEmail(email).get();
    }
}
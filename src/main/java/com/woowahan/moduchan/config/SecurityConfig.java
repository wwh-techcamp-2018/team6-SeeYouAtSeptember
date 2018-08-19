package com.woowahan.moduchan.config;

import com.woowahan.moduchan.interceptor.ApiUserSessionInterceptor;
import com.woowahan.moduchan.interceptor.UserSessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApiUserSessionInterceptor apiUserSessionInterceptor() {
        return new ApiUserSessionInterceptor();
    }

    //현제 필요하지 않은 인터셉터 일반 컨트롤러에서 로그인 체크 또는 어드민 체크를 할 때 등록해야 함
    @Bean
    public UserSessionInterceptor userSessionInterceptor() {
        return new UserSessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(apiUserSessionInterceptor())
                .addPathPatterns("/api/**/chk/**");
    }
}

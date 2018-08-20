package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private NormalUserRepository normalUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getNomalUsers() {
        // TODO: 2018. 8. 14. 만약에 아무 유저도 없는 경우에는 에러로 처리할 것인가 그냥 반환할 것인가?
        return normalUserRepository.findAll().stream()
                .map(normalUser -> normalUser.toDTO())
                .collect(Collectors.toList());
    }

    public UserDTO getNormalUser(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        return normalUserRepository.findById(uid)
                .map(normalUser -> normalUser.toDTO())
                .orElseThrow(RuntimeException::new);
    }

    public UserDTO createNormalUser(UserDTO userDTO) {
        // TODO: 2018. 8. 16. 같은 이메일에 대해서 에러 처리한다.
        return normalUserRepository
                .save(NormalUser.from(userDTO).encryptPassword(passwordEncoder))
                .toDTO().erasePassword();
    }

    public void updateNormalUser(UserDTO userDTO) {
        // FIXME: 2018. 8. 15. 세션에 기록된 id를 이용해서 DB로부터 유저를 꺼내어 정보 갱신을 해야합니다.
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        normalUserRepository.save(
                normalUserRepository.findByEmail(userDTO.getEmail())
                        .orElseThrow(RuntimeException::new).update(userDTO));
    }

    @Transactional
    public void deleteNormalUserById(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        normalUserRepository.findById(uid)
                .orElseThrow(RuntimeException::new).delete();
    }

    public UserDTO login(UserDTO userDTO) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        NormalUser loginUser = normalUserRepository
                .findByEmail(userDTO.getEmail())
                .orElseThrow(RuntimeException::new);

        if (!loginUser.matchPassword(userDTO.getPassword(), passwordEncoder)) {
            // TODO: 2018. 8. 16. PasswordNotMatched(Forbidden)
            throw new RuntimeException();
        }

        return loginUser.toDTO().erasePassword();
    }
}

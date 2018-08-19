package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.UserDTO;
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

    public List<UserDTO> findAllNomalUser() {
        // TODO: 2018. 8. 14. 만약에 아무 유저도 없는 경우에는 에러로 처리할 것인가 그냥 반환할 것인가?
        return normalUserRepository.findAllByDeletedFalse().stream()
                .map(normalUser -> normalUser.toDTO())
                .collect(Collectors.toList());
    }

    public UserDTO findNormalUserById(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        return normalUserRepository.findByIdAndDeletedFalse(uid)
                .map(normalUser -> normalUser.toDTO())
                .orElseThrow(RuntimeException::new);
    }

    public UserDTO createNormalUser(UserDTO userDTO) {
        // TODO: 2018. 8. 19. CustomError: AlreadyExistsEmail
        if (normalUserRepository.existsByEmail(userDTO.getEmail()))
            throw new RuntimeException();
        return normalUserRepository
                .save(NormalUser.from(userDTO).encryptPassword(passwordEncoder))
                .toDTO().erasePassword();
    }

    public void updateNormalUser(UserDTO userDTO) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        normalUserRepository.save(
                normalUserRepository.findByIdAndDeletedFalse(userDTO.getId())
                        .orElseThrow(RuntimeException::new).update(userDTO));
    }

    @Transactional
    public void deleteNormalUserById(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        normalUserRepository.findByIdAndDeletedFalse(uid)
                .orElseThrow(RuntimeException::new).delete();
    }

    public UserDTO login(UserDTO userDTO) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        NormalUser loginUser = normalUserRepository
                .findByEmailAndDeletedFalse(userDTO.getEmail())
                .orElseThrow(RuntimeException::new);

        if (!loginUser.matchPassword(userDTO.getPassword(), passwordEncoder)) {
            // TODO: 2018. 8. 16. PasswordNotMatched(Forbidden)
            throw new RuntimeException();
        }

        return loginUser.toDTO().erasePassword();
    }
}

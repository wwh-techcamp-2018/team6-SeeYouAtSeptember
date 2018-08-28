package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.EmailAlreadyExistsException;
import com.woowahan.moduchan.exception.PasswordNotMatchedException;
import com.woowahan.moduchan.exception.UserNotFoundException;
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
        return normalUserRepository.findAll().stream()
                .map(normalUser -> normalUser.toDTO())
                .collect(Collectors.toList());
    }

    public UserDTO getNormalUser(Long uid) {
        return normalUserRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException())
                .toDTO();
    }

    public UserDTO createNormalUser(UserDTO userDTO) {
        if (normalUserRepository.existsByEmail(userDTO.getEmail()))
            throw new EmailAlreadyExistsException();
        return normalUserRepository
                .save(NormalUser.from(userDTO).encryptPassword(passwordEncoder))
                .toDTO().erasePassword();
    }

    public UserDTO updateNormalUser(UserDTO userDTO) {
        return normalUserRepository.save(
                normalUserRepository.findById(userDTO.getUid())
                        .orElseThrow(() -> new UserNotFoundException())
                        .update(userDTO))
                .toDTO().erasePassword();
    }

    @Transactional
    public void deleteNormalUserById(Long uid) {
        normalUserRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException())
                .delete();
    }

    public UserDTO login(UserDTO userDTO) {
        NormalUser loginUser = normalUserRepository
                .findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        if (!loginUser.matchPassword(userDTO.getPassword(), passwordEncoder)) {
            throw new PasswordNotMatchedException();
        }

        return loginUser.toDTO().erasePassword();
    }
}

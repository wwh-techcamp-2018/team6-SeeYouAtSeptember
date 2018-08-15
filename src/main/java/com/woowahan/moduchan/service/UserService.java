package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.repository.NormalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private NormalUserRepository normalUserRepository;

    public List<UserDTO> findAllNomalUser() {
        // TODO: 2018. 8. 14. 만약에 아무 유저도 없는 경우에는 에러로 처리할 것인가 그냥 반환할 것인가?
        return normalUserRepository.findAllByDeletedNot().stream()
                .map(normalUser -> normalUser.toDTO().erasePassword())
                .collect(Collectors.toList());
    }

    public UserDTO findNormalUserById(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        return normalUserRepository.findByIdAndDeletedNot(uid)
                .map(normalUser -> normalUser.toDTO().erasePassword())
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public void deleteNormalUserById(Long uid) {
        // TODO: 2018. 8. 14. CustomError: UserNotFound
        normalUserRepository.findByIdAndDeletedNot(uid)
                .orElseThrow(RuntimeException::new).delete();
    }
}

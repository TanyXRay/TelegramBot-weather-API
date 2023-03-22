package ru.home.chernyadieva.springweatherapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.chernyadieva.springweatherapp.repository.entity.UserEntity;
import ru.home.chernyadieva.springweatherapp.repository.UserEntityRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserEntityRepository userEntityRepository;

    public UserService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Transactional
    public void saveUser(UserEntity userEntity) {
        userEntityRepository.save(userEntity);
    }

    public Optional<UserEntity> findByUserId(long userId) {
        return userEntityRepository.findByUserId(userId);
    }
}

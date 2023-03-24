package ru.home.chernyadieva.springweatherapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.chernyadieva.springweatherapp.repository.UserEntityRepository;
import ru.home.chernyadieva.springweatherapp.repository.entity.UserEntity;

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

    @Transactional
    public void updateUser(UserEntity updatedUserEntity, int id) {
        updatedUserEntity.setId(id);

        userEntityRepository.save(updatedUserEntity);
    }

    @Transactional
    public void delete(int id) {
        userEntityRepository.deleteById(id);
    }

    public Optional<UserEntity> findByUserId(long userId) {
        return userEntityRepository.findByUserId(userId);
    }
}

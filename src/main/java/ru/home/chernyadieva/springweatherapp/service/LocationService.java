package ru.home.chernyadieva.springweatherapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.chernyadieva.springweatherapp.repository.entity.LocationEntity;
import ru.home.chernyadieva.springweatherapp.repository.LocationEntityRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationEntityRepository locationEntityRepository;

    public LocationService(LocationEntityRepository locationEntityRepository) {
        this.locationEntityRepository = locationEntityRepository;
    }

    @Transactional
    public void saveLocation(LocationEntity locationEntity) {
        locationEntityRepository.save(locationEntity);
    }

    public Optional<LocationEntity> findByUserId(long userId) {
        return locationEntityRepository.findByUserId(userId);
    }
}

package ru.home.chernyadieva.springweatherapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.chernyadieva.springweatherapp.repository.entity.LocationEntity;

import java.util.Optional;

@Repository
public interface LocationEntityRepository extends JpaRepository<LocationEntity, Integer> {

    Optional<LocationEntity> findByUserId(long userId);

    Optional<LocationEntity> findByUserIdAndUserLocationId(long userId, int locationId);
}

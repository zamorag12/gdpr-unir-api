package com.gdpr.unir.cities.repository;


import com.gdpr.unir.cities.model.Cities;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesRepository extends CrudRepository<Cities, Long> {
    boolean existsByUserIdAndLocation(Long userId, final String location);
}

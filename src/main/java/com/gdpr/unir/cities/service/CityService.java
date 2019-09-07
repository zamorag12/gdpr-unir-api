package com.gdpr.unir.cities.service;

import com.gdpr.unir.cities.model.Cities;
import com.gdpr.unir.cities.model.CityRequest;
import com.gdpr.unir.cities.repository.CitiesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CityService {
    private final CitiesRepository citiesRepository;

    public CityService(final CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public Cities create(final CityRequest cityRequest) {

        final String name = cityRequest.getName();
        final String location = cityRequest.getLocation();
        final Long userId = cityRequest.getUserId();

        final Cities cities = Cities.builder()
                .name(name)
                .location(location)
                .userId(userId)
                .build();

        return citiesRepository.save(cities);
    }
}

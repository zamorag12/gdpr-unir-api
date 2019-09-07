package com.gdpr.unir.cities;

import com.gdpr.unir.cities.model.Cities;
import com.gdpr.unir.cities.model.CityRequest;
import com.gdpr.unir.cities.model.CityResource;
import com.gdpr.unir.cities.repository.CitiesRepository;
import com.gdpr.unir.cities.service.CityService;
import com.gdpr.unir.converter.CityResourceConverter;
import com.gdpr.unir.exception.impl.IllegalStateException;
import com.gdpr.unir.exception.impl.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/v1/cities")
public class CitiesController {
    private final CityService cityService;
    private final CityResourceConverter cityResourceConverter;
    private final CitiesRepository citiesRepository;

    public CitiesController(final CityService userService,
                            final CityResourceConverter cityResourceConverter,
                            final CitiesRepository userRepository) {
        this.cityService = userService;
        this.cityResourceConverter = cityResourceConverter;
        this.citiesRepository = userRepository;
    }

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public CityResource createCity(@RequestBody @Valid final CityRequest cityRequest) {
        final String name = cityRequest.getName();
        final String location = cityRequest.getLocation();
        final Long userId = cityRequest.getUserId();

        final boolean cityExists = citiesRepository
                .existsByUserIdAndLocation(userId, location);

        if (!cityExists) {
            final Cities cities = cityService
                    .create(cityRequest);

            return cityResourceConverter.convert(cities);
        } else {
            final String errorMessage = "Reservation's city " + name + " already exists in the system.";

            log.info(errorMessage);

            throw IllegalStateException.builder()
                    .message(errorMessage)
                    .errorMessageKey("error.city.already-exists")
                    .addAdditionalInformation("name", name)
                    .build();
        }
    }


    @GetMapping(value = "/{cityId}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(OK)
    public CityResource getCity(@PathVariable final Long cityId) {
        final Cities cities = citiesRepository.findById(cityId).orElseThrow(() -> ResourceNotFoundException.builder()
                .message("City's reservation not found")
                .errorMessageKey("error.city.not-found")
                .addAdditionalInformation("cityId", cityId)
                .build());

        return cityResourceConverter.convert(cities);
    }
}

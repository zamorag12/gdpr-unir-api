package com.gdpr.unir.converter;

import com.gdpr.unir.cities.model.Cities;
import com.gdpr.unir.cities.model.CityResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CityResourceConverter implements Converter<Cities, CityResource> {
    @Override
    public CityResource convert(final Cities cities) {
        return CityResource.builder()
                .id(cities.getId())
                .name(cities.getName())
                .location(cities.getLocation())
                .userId(cities.getUserId())
                .build();
    }
}

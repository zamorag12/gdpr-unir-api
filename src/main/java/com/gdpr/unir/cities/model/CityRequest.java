package com.gdpr.unir.cities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CityRequest {
    @NotBlank(message = "{user.city-name.not-blank}")
    private String name;

    @NotBlank(message = "{user.city-location.not-blank}")
    private String location;

    @NotNull(message = "{user.city-user-id.not-null}")
    private Long userId;
}

package com.gdpr.unir.converter;

import com.gdpr.unir.users.model.User;
import com.gdpr.unir.users.model.UserResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserResourceConverter implements Converter<User, UserResource> {
    @Override
    public UserResource convert(final User user) {
        return UserResource.builder()
                .status(user.getStatus().name())
                .userId(user.getId())
                .build();
    }
}

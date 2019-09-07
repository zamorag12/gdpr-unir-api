package com.gdpr.unir.converter;

import com.gdpr.unir.users.model.User;
import com.gdpr.unir.users.model.UserDataResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDataResourceConverter implements Converter<User, UserDataResource> {
    @Override
    public UserDataResource convert(final User user) {
        return UserDataResource.builder()
                .status(user.getStatus().name())
                .userName(user.getUserName())
                .userId(user.getId())
                .userIdentityDocumentType(user.getUserIdentityDocumentType().name())
                .userIdentityDocumentCode(user.getUserIdentityDocumentCode())
                .userIdentityDocumentCountryCode(user.getUserIdentityDocumentCountryCode())
                .createdAt(user.getCreatedAt())
                .createdBy(user.getCreatedBy())
                .build();
    }
}

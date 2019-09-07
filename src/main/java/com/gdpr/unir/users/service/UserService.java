package com.gdpr.unir.users.service;

import com.gdpr.unir.users.model.SecurityPasswordUser;
import com.gdpr.unir.users.model.SecurityUser;
import com.gdpr.unir.users.model.User;
import com.gdpr.unir.users.model.UserRequest;
import com.gdpr.unir.users.repository.SecurityPasswordUserRepository;
import com.gdpr.unir.users.repository.SecurityUserRepository;
import com.gdpr.unir.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.gdpr.unir.users.model.User.UserStatus.CONFIRMED;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUserRepository securityUserRepository;
    private final SecurityPasswordUserRepository securityPasswordUserRepository;

    public UserService(final UserRepository userRepository,
                       final SecurityUserRepository securityUserRepository,
                       final SecurityPasswordUserRepository securityPasswordUserRepository) {
        this.userRepository = userRepository;
        this.securityUserRepository = securityUserRepository;
        this.securityPasswordUserRepository = securityPasswordUserRepository;
    }

    public User create(final UserRequest userRequest) {

        final String username = userRequest.getUserName();
        final String userEmail = userRequest.getUserEmail();
        final String userIdentityDocumentCode = userRequest.getUserIdentityDocumentCode();
        final String userIdentityDocumentCountryCode = userRequest.getUserIdentityDocumentCountryCode();

        final String source = userRequest.getSource();
        final String userIdentityDocumentType = userRequest.getUserIdentityDocumentType();

        final User user = User.builder()
                .userName(username)
                .userEmail(userEmail)
                .userIdentityDocumentCode(userIdentityDocumentCode)
                .userIdentityDocumentType(User.UserDocumentType.valueOf(userIdentityDocumentType))
                .userIdentityDocumentCountryCode(userIdentityDocumentCountryCode)
                .status(CONFIRMED)
                .createdAt(new Date())
                .createdBy(source)
                .build();

        final SecurityUser securityUser = SecurityUser.builder()
                .active(true)
                .id(username)
                .idRole("ADMINISTRATOR")
                .username(username)
                .build();

        securityUserRepository.save(securityUser);

        final SecurityPasswordUser securityPasswordUser = SecurityPasswordUser.builder()
                .password("$2a$10$wT/obdcvI07jSor8roN3NeuWhEdlarvunJ14ZOAFdo1g/lZ1uHbGe")
                .idUser(username)
                .build();

        securityPasswordUserRepository.save(securityPasswordUser);

        return userRepository.save(user);
    }
}

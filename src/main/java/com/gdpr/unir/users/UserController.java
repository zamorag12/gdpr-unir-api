package com.gdpr.unir.users;

import com.gdpr.unir.converter.UserDataResourceConverter;
import com.gdpr.unir.converter.UserResourceConverter;
import com.gdpr.unir.exception.impl.IllegalStateException;
import com.gdpr.unir.exception.impl.ResourceNotFoundException;
import com.gdpr.unir.users.model.User;
import com.gdpr.unir.users.model.UserDataResource;
import com.gdpr.unir.users.model.UserRequest;
import com.gdpr.unir.users.model.UserResource;
import com.gdpr.unir.users.repository.UserRepository;
import com.gdpr.unir.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/v1/users")
public class UserController {
    private final UserService userService;
    private final UserResourceConverter userResourceConverter;
    private final UserDataResourceConverter userDataResourceConverter;
    private final UserRepository userRepository;

    public UserController(final UserService userService,
                          final UserResourceConverter userResourceConverter,
                          final UserDataResourceConverter userDataResourceConverter,
                          final UserRepository userRepository) {
        this.userService = userService;
        this.userResourceConverter = userResourceConverter;
        this.userDataResourceConverter = userDataResourceConverter;
        this.userRepository = userRepository;
    }

    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public UserResource createUser(@RequestBody @Valid final UserRequest userRequest) {
        final String username = userRequest.getUserName();
        final String userEmail = userRequest.getUserEmail();
        final String userIdentityDocumentType = userRequest.getUserIdentityDocumentType();

        final boolean userExists = userRepository
                .existsByUserEmailAndUserIdentityDocumentType(userEmail, User.UserDocumentType.valueOf(userIdentityDocumentType));

        if (!userExists) {
            final User userCreated = userService
                    .create(userRequest);

            return userResourceConverter.convert(userCreated);
        } else {
            final String errorMessage = "User " + username + " already exists in the system.";

            log.info(errorMessage);

            throw IllegalStateException.builder()
                    .message(errorMessage)
                    .errorMessageKey("error.user.already-exists")
                    .addAdditionalInformation("username", username)
                    .build();
        }
    }

    @GetMapping(value = "/{idUser}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(OK)
    public UserDataResource getUserById(@PathVariable final Long idUser) {
        final User user = userRepository.findById(idUser).orElseThrow(() -> ResourceNotFoundException.builder()
                .message("User not found")
                .errorMessageKey("error.user.not-found")
                .addAdditionalInformation("idUser", idUser)
                .build());

        return userDataResourceConverter.convert(user);
    }

    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(OK)
    public UserDataResource getUserByEmail(@Email @RequestParam("email") final String userEmail) {
        final User user = userRepository.findByUserEmail(userEmail).orElseThrow(() -> ResourceNotFoundException.builder()
                .message("User not found")
                .errorMessageKey("error.user.not-found")
                .addAdditionalInformation("userEmail", userEmail)
                .build());

        return userDataResourceConverter.convert(user);
    }
}

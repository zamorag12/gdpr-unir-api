package com.gdpr.unir.users.repository;


import com.gdpr.unir.users.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUserEmailAndUserIdentityDocumentType(String userEmail, User.UserDocumentType userIdentityDocumentType);

    Optional<User> findByUserEmail(String userEmail);
}

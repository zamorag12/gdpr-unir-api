package com.gdpr.unir.users.repository;


import com.gdpr.unir.users.model.SecurityUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityUserRepository extends CrudRepository<SecurityUser, String> {
}

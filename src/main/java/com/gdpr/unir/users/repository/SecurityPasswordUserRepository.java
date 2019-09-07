package com.gdpr.unir.users.repository;


import com.gdpr.unir.users.model.SecurityPasswordUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityPasswordUserRepository extends CrudRepository<SecurityPasswordUser, String> {
}

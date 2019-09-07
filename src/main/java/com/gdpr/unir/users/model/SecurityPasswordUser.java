package com.gdpr.unir.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_security_user_password")
public class SecurityPasswordUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    private String idUser;

    @Basic(optional = false)
    private String password;
}

package com.gdpr.unir.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "c_security_user")
public class SecurityUser {
    @Id
    private String id;

    @Basic(optional = false)
    private boolean active;

    @Basic(optional = false)
    private String username;

    @Basic(optional = false)
    private String idRole;
}

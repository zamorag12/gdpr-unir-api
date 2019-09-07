package com.gdpr.unir.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site_users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Basic(optional = false)
    private String userName;

    @Basic(optional = false)
    private String userEmail;

    @Enumerated(STRING)
    private UserDocumentType userIdentityDocumentType;

    private String userIdentityDocumentCode;
    private String userIdentityDocumentCountryCode;
    private UserStatus status;

    @Temporal(TIMESTAMP)
    private Date createdAt;

    @Basic(optional = false)
    @Column(updatable = false)
    private String createdBy;

    @Temporal(TIMESTAMP)
    private Date updatedAt;

    private String updatedBy;

    public enum UserStatus {
        CONFIRMED, CLOSED
    }

    public enum UserDocumentType {
        PASSPORT, UID, OTHER
    }
}

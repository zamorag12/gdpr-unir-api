package com.gdpr.unir.users.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataResource {
    private Long userId;
    private String status;
    private String userName;
    private String userIdentityDocumentType;
    private String userIdentityDocumentCode;
    private String userIdentityDocumentCountryCode;
    private Date createdAt;
    private String createdBy;
}

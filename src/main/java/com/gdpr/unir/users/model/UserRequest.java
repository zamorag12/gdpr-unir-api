package com.gdpr.unir.users.model;

import com.gdpr.unir.validator.ValidEnumValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "{user.user-name.not-blank}")
    @Length(max = 256, message = "{user.user-name.max-length}")
    private String userName;

    @NotBlank(message = "{user.user-email.not-blank}")
    @Length(max = 256, message = "{user.user-email.max-length}")
    private String userEmail;

    @ValidEnumValue(enumClass = User.UserDocumentType.class, message = "{user.document-type.invalid-value}")
    private String userIdentityDocumentType;

    @Length(max = 64, message = "{user.user-document-code.max-length}")
    private String userIdentityDocumentCode;

    @Length(max = 3, message = "{user.user-document-country-code.max-length}")
    private String userIdentityDocumentCountryCode;

    @NotBlank(message = "{source.not-blank}")
    private String source;
}

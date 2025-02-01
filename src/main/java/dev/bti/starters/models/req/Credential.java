package dev.bti.starters.models.req;

import dev.bti.starters.enums.CredentialType;
import dev.bti.starters.util.RegexUtils;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Credential {
    private String credential;
    private CredentialType credentialType;

    public static Credential of(String credential) {
        CredentialBuilder credentialBuilder = new CredentialBuilder();
        credentialBuilder.credential = credential;

        if (credential.contains("@")) {
            credentialBuilder.credentialType = CredentialType.EMAIL;
        } else if (RegexUtils.isAlphanumeric(credential) && credential.length() == 16) {
            credentialBuilder.credentialType = CredentialType.UID;
        } else {
            credentialBuilder.credentialType = CredentialType.PHONE;
        }

        return credentialBuilder.build();
    }

    public void transform(CredentialType credentialType, String newCredential) {
        setCredential(newCredential);
        setCredentialType(credentialType);
    }
}

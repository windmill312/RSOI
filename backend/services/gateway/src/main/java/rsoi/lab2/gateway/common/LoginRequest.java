package rsoi.lab2.gateway.common;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class LoginRequest {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    private String redirectUri;

    //TODO не забыть выставить на морде
    private UUID serviceUuid;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public UUID getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }
}

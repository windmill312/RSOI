package rsoi.lab2.payload;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    private String redirectUri;

    //todo не забыть выставить на морде
    private UUID serviceUuid;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
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

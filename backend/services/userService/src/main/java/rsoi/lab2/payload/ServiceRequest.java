package rsoi.lab2.payload;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class ServiceRequest {
    @NotBlank
    private UUID uuid;

    @NotBlank
    private String redirectUri;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}

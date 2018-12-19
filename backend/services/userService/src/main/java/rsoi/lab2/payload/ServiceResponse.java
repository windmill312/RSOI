package rsoi.lab2.payload;


import java.util.UUID;

public class ServiceResponse extends ApiResponse {

    private UUID uuid;
    private String secretKey;

    public ServiceResponse(Boolean success, String message, UUID uuid, String secretKey) {
        super(success, message);
        this.uuid = uuid;
        this.secretKey = secretKey;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}

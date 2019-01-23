package rsoi.lab2.payload;

import java.util.UUID;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;
    private int jwtExpirationInMs;
    private boolean isAdmin = false;
    private UUID code;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, int jwtExpirationInMs, UUID authCode) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.jwtExpirationInMs = jwtExpirationInMs;
        this.code = authCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }

    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }
}

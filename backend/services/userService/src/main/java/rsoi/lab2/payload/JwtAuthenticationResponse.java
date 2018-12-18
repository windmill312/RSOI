package rsoi.lab2.payload;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String refreshToken;
    private int jwtExpirationInMs;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, int jwtExpirationInMs) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.jwtExpirationInMs = jwtExpirationInMs;
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
}

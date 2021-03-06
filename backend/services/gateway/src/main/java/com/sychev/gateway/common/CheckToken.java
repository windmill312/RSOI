package com.sychev.gateway.common;

import com.sychev.gateway.model.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public class CheckToken {

    public static boolean checkToken (String accessToken, String userUuid, String serviceUuid, String gatewayUuid) throws HttpClientErrorException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl = "http://localhost:8084/api/auth/validate?gatewayUuid=" + gatewayUuid;
            UserRequest userRequest = new UserRequest(accessToken, UUID.fromString(userUuid), UUID.fromString(serviceUuid));
            ResponseEntity<Object> response = restTemplate.postForEntity(resourceUrl, userRequest, Object.class);
            return response.getStatusCode() == HttpStatus.OK;
        }
        catch (HttpClientErrorException e) {
            return false;
        }
    }
}

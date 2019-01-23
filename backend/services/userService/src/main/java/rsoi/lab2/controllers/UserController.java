package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rsoi.lab2.payload.UserSummary;
import rsoi.lab2.security.CurrentUser;
import rsoi.lab2.security.UserPrincipal;

@RestController
@RequestMapping("/api")
public class UserController {

    @Value("${app.gatewayUuid}")
    private String gateway;

    @GetMapping(
            value = "/user/me",
            params = {
                    "gatewayUuid"
            })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getCurrentUser(@CurrentUser UserPrincipal currentUser,
                                         @RequestParam String gatewayUuid) {
        if (gatewayUuid.equals(gateway)) {
            UserSummary userSummary = new UserSummary(currentUser.getUuid(), currentUser.getUsername(), currentUser.getName());
            return ResponseEntity.ok().body(userSummary);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}

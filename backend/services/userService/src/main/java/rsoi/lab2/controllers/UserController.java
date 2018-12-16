package rsoi.lab2.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rsoi.lab2.payload.UserSummary;
import rsoi.lab2.security.CurrentUser;
import rsoi.lab2.security.UserPrincipal;

@RestController
@RequestMapping("/api")
public class UserController {
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getUuid(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }
}

package rsoi.lab2.services;

import org.springframework.lang.Nullable;
import rsoi.lab2.entity.User;
import rsoi.lab2.model.UserInfo;
import rsoi.lab2.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface UserService {

    @Nullable
    List<UserInfo> listAll();

    UserPrincipal loadUserByUsername(String usernameOrEmail);

    User saveOrUpdate(User user);

}

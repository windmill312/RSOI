package rsoi.lab2.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import rsoi.lab2.entity.User;
import rsoi.lab2.model.UserInfo;

import java.util.List;
import java.util.UUID;

public interface UserService {

    @NonNull
    List<UserInfo> listAll();

    @Nullable
    UserInfo getUserInfoByUid(UUID uid);

    @Nullable
    User getUserByUid(UUID uid);

    @Nullable
    UserInfo getUserInfoByLogin(String login);

    @Nullable
    User getUserByLogin(String login);

    @Nullable
    UserInfo getUserInfoByToken(UUID token);

    @Nullable
    User getUserByToken(UUID token);

    User saveOrUpdate(User user);

}

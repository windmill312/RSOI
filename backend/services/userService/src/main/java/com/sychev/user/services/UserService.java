package com.sychev.user.services;

import com.sychev.user.entity.User;
import com.sychev.user.model.UserInfo;
import com.sychev.user.security.UserPrincipal;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserService {

    @Nullable
    List<UserInfo> listAll();

    UserPrincipal loadUserByUsername(String usernameOrEmail);

    User saveOrUpdate(User user);

}

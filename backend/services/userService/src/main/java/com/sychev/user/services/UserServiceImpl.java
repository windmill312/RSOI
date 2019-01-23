package com.sychev.user.services;

import com.sychev.user.entity.User;
import com.sychev.user.model.UserInfo;
import com.sychev.user.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sychev.user.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @NonNull
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserInfo> listAll() {
        return userRepository.findAll().stream().map(this::buildUserInfo).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserPrincipal loadUserByUuid(UUID uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + uuid)
        );

        return UserPrincipal.create(user);
    }

    private UserInfo buildUserInfo(User user) {
        UserInfo info = new UserInfo();
        info.setIdUser(user.getId());
        info.setName(user.getName());
        info.setUsername(user.getUsername());
        info.setLogin(user.getEmail());
        info.setPassword(user.getPassword());
        return info;
    }

    @Override
    public User saveOrUpdate(User user) {
        userRepository.save(user);
        return user;
    }
}

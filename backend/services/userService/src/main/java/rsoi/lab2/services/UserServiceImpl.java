package rsoi.lab2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import rsoi.lab2.entity.User;
import rsoi.lab2.model.UserInfo;
import rsoi.lab2.repositories.UserRepository;

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

    private UserInfo buildUserInfo(User user) {
        UserInfo info = new UserInfo();
        info.setIdUser(user.getIdUser());
        info.setFirstName(user.getFirstName());
        info.setSecondName(user.getSecondName());
        info.setLogin(user.getLogin());
        info.setPassword(user.getPassword());
        info.setUid(user.getUid());
        return info;
    }

    @Override
    public UserInfo getUserInfoByUid(UUID uid) {
        return buildUserInfo(userRepository.findByUid(uid));
    }

    @Override
    public User getUserByUid(UUID uid) {
        return userRepository.findByUid(uid);
    }

    @Override
    public UserInfo getUserInfoByLogin(String login) {
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User saveOrUpdate(User user) {
        userRepository.save(user);
        return user;
    }
}

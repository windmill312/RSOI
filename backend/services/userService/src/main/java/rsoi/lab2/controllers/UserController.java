package rsoi.lab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.User;
import rsoi.lab2.model.UserInfo;
import rsoi.lab2.services.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class UserController {

    Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        logger.info("Get \"ping\" request.");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity listUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "5") int size) {
        logger.info("Get \"users\" request with params (page=" + page + ", size=" + size + ").");
        List<UserInfo> list = userService.listAll();
        if ((size * page) > list.size())
            return ResponseEntity.ok(list.subList((size * (page - 1)), (size * page) - ((size * page) - list.size())));
        else
            return ResponseEntity.ok(list.subList(size * (page - 1), size * page));
    }

    @GetMapping(value = "/user",
            params = "uidUser")
    public ResponseEntity getUser(@RequestParam String uidUser) {
        logger.info("Get \"user\" request with param (uidUser=" + uidUser + ").");
        return ResponseEntity.ok(userService.getUserInfoByUid(UUID.fromString(uidUser)));
    }

    @PutMapping("/user")
    public ResponseEntity add(@RequestBody UserInfo userInfo) {
        try {
            logger.info("Get PUT request (add) with params (login=" + userInfo.getLogin() + ", password=" + userInfo.getPassword() + ").");
            User user = new User();
            if (userInfo.getIdUser() != 0)
                user.setIdUser(userInfo.getIdUser());
            user.setFirstName(userInfo.getFirstName());
            user.setSecondName(userInfo.getSecondName());
            user.setBirthDate(userInfo.getBirthDate());
            user.setLogin(userInfo.getLogin());
            user.setPassword(userInfo.getPassword());
            user.setUid(UUID.randomUUID());
            userService.saveOrUpdate(user);
            return ResponseEntity.ok(user.getUid().toString());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/user")
    public ResponseEntity edit(@RequestBody UserInfo userInfo) {
        logger.info("Get PATCH request (edit) with params (login=" + userInfo.getLogin() + ", password=" + userInfo.getPassword() + ").");
        User user = userService.getUserByUid(userInfo.getUid());
        user.setFirstName(userInfo.getFirstName());
        userService.saveOrUpdate(user);
        return ResponseEntity.ok().build();
    }

}

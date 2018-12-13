package rsoi.lab2.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rsoi.lab2.entity.User;
import rsoi.lab2.model.UserInfo;
import rsoi.lab2.services.UserService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
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

    @GetMapping(value = "/user")
    public ResponseEntity auth(@RequestBody String login, @RequestBody String password) {
        logger.info("Auth request with param (login=" + login + ", password=" + password + ").");
        User user = userService.getUserByLogin(login);
        if (user.getLogin().equals(login) && user.getPassword().equals(password)) {

            user.setDttmCreateToken(Timestamp.valueOf(LocalDateTime.now()));
            user.setToken(UUID.randomUUID());
            user.setRefreshToken(UUID.randomUUID());
            userService.saveOrUpdate(user);

            String jsonString = new JSONObject()
                    .put("token", user.getToken())
                    .put("refreshToken", user.getRefreshToken()).toString();

            return ResponseEntity.status(HttpStatus.OK).body(jsonString);
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //клиент не найден
    }

    @GetMapping(value = "/user",
            params = "token")
    public ResponseEntity checkToken(@RequestParam String token) {
        logger.info("Check token request with param (token=" + token + ").");
        UserInfo userInfo = userService.getUserInfoByToken(UUID.fromString(token));
        if (! userInfo.equals(null)) {

            Timestamp original = userInfo.getDttmCurrentToken();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(original.getTime());
            cal.add(Calendar.MINUTE, 30);
            Timestamp later = new Timestamp(cal.getTime().getTime());

            if (later.before(Timestamp.valueOf(LocalDateTime.now())))
                return ResponseEntity.status(HttpStatus.FOUND).build();//клиент найден
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //token просрочился
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //клиент не найден
    }

    @PutMapping("/user")
    public ResponseEntity registry(@RequestBody UserInfo userInfo) {
        try {
            logger.info("Get PUT request (add) with params (login=" + userInfo.getLogin() + ", password=" + userInfo.getPassword() + ").");

            UserInfo checkUser = userService.getUserInfoByLogin(userInfo.getLogin());
            if (checkUser.equals(null)) {
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
                return ResponseEntity.status(HttpStatus.CREATED).body(user.getUid());
            }
            else
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/user")
    public ResponseEntity edit(@RequestBody UserInfo userInfo) {
        logger.info("Get PATCH request (edit) with params (login=" + userInfo.getLogin() + ", password=" + userInfo.getPassword() + ").");
        User user = userService.getUserByUid(userInfo.getUid());
        if (! user.equals(null)) {
            if (!userInfo.getFirstName().isEmpty()) {
                user.setFirstName(userInfo.getFirstName());
            }
            if (!userInfo.getSecondName().isEmpty()) {
                user.setSecondName(userInfo.getSecondName());
            }
            if (!userInfo.getBirthDate().isEmpty()) {
                user.setBirthDate(userInfo.getBirthDate());
            }
            if (!userInfo.getLogin().isEmpty()) {
                user.setLogin(userInfo.getLogin());
            }
            if (!userInfo.getPassword().isEmpty()) {
                user.setPassword(userInfo.getPassword());
            }
            userService.saveOrUpdate(user);
            return ResponseEntity.ok().build();
        }
        else {
            logger.info("userUid is null");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

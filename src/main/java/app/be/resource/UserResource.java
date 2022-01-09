package app.be.resource;

import app.be.model.User;
import app.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(final HttpServletRequest request,
                                        final String userName,
                                        final String password) {
        final User users = userService.insertUser(userName, password);
        return ResponseEntity.ok().body(users);
    }

    @GetMapping(value = "/info",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(final HttpServletRequest request) {

        LOGGER.info("request {}", request);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/check",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> check(final HttpServletRequest request) {
        List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }

        return ResponseEntity.ok().body(null);
    }


    @GetMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginUser(final HttpServletRequest request,
                                            final String email,
                                            final String password) {

        User user = userService.findUser(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email");
        }
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Bad password");
        }
        return ResponseEntity.ok().body("Login Success");
    }

}

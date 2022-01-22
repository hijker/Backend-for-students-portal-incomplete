package app.be.resource;

import app.be.model.SocietyUser;
import app.be.model.User;
import app.be.response.StandardResponse;
import app.be.service.SocietyUserService;
import app.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/society_auth")
public class SocietyAuthResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyAuthResource.class);

    @Autowired
    private SocietyUserService societyUserService;

    static Map<String, SocietyUser> activeUsers = new ConcurrentHashMap<>();

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SocietyUser> addUser(final HttpServletRequest request,
                                               final String name,
                                               final String email,
                                               final String password) {
        final SocietyUser user = societyUserService.insertUser(name, email, password);
        user.setPassword("");
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> loginUser(final HttpServletRequest request,
                                                      final String email,
                                                      final String key) {
        SocietyUser user = societyUserService.findUser(email);

        if (user == null) {
            return ResponseEntity.badRequest().body(new StandardResponse("Invalid email"));
        }
        if (!user.getPassword().equals(key)) {
            return ResponseEntity.badRequest().body(new StandardResponse("Bad password"));
        }
        activeUsers.put(request.getSession().getId(), user);
        return ResponseEntity.ok().body(new StandardResponse("Login Success"));
    }

    @PostMapping(value = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> logout(final HttpServletRequest request) {
        activeUsers.remove(request.getSession().getId());
        return ResponseEntity.ok().body(new StandardResponse("Logged Out"));
    }

    @GetMapping(value = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SocietyUser> currentUser(final HttpServletRequest request) {
        SocietyUser user = activeUsers.get(request.getSession().getId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }
}

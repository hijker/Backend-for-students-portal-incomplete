package app.be.resource;

import app.be.model.User;
import app.be.response.StandardResponse;
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
@RequestMapping("/auth")
public class AuthResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyAuthResource.class);
    static Map<String, Integer> otps = new ConcurrentHashMap<>();
    static Map<String, User> activeUsers = new ConcurrentHashMap<>();
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender emailSender;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(final HttpServletRequest request,
                                        final String name,
                                        final String email,
                                        final String type,
                                        final String password) {
        final User user = userService.insertUser(name, email, type, password);
        user.setPassword("");
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> loginUser(final HttpServletRequest request,
                                                      final String email,
                                                      final String key) {
        User user = userService.findUser(email);

        if (user == null) {
            return ResponseEntity.badRequest().body(new StandardResponse("Invalid email"));
        }
        if (user.getType().equalsIgnoreCase("student")) {
            int otp;
            try {
                otp = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(new StandardResponse("Wrong OTP"));
            }
            if (otps.getOrDefault(request.getSession().getId(), -1) != otp) {
                return ResponseEntity.badRequest().body(new StandardResponse("Wrong OTP"));
            }
        }
        if (!user.getPassword().equals(key)) {
            return ResponseEntity.badRequest().body(new StandardResponse("Bad password"));
        }
        activeUsers.put(request.getSession().getId(), user);
        return ResponseEntity.ok().body(new StandardResponse("Login Success"));
    }

    @GetMapping(value = "/authType",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> authType(final HttpServletRequest request,
                                                     final String email) {

        User user = userService.findUser(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new StandardResponse("Invalid email"));
        }
        if (user.getType().equalsIgnoreCase("student")) {
            //Send OTP
            int random = (int) (System.currentTimeMillis() + new Random().nextInt(100000000)) % 1000000;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@iit.com");
            message.setTo(email);
            message.setSubject("Login OTP");
            otps.put(request.getSession().getId(), random);
            message.setText("Your OTP is : " + random);
            emailSender.send(message);
            return ResponseEntity.ok().body(new StandardResponse("OTP"));
        }

        return ResponseEntity.ok().body(new StandardResponse("PASSWORD"));
    }

    @PostMapping(value = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> logout(final HttpServletRequest request) {
        activeUsers.remove(request.getSession().getId());
        return ResponseEntity.ok().body(new StandardResponse("Logged Out"));
    }

    @GetMapping(value = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> currentUser(final HttpServletRequest request) {
        User user = activeUsers.get(request.getSession().getId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(user);
    }
}

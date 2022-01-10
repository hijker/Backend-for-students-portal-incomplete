package app.be.resource;

import app.be.model.User;
import app.be.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender emailSender;

    static Map<String, Integer> otps = new ConcurrentHashMap<>();

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

        request.getSession().setAttribute("MY_SESSION_MESSAGES", Arrays.asList(System.currentTimeMillis() + ""));
        if (messages == null) {
            messages = new ArrayList<>();
        }

        System.out.println("Session id : " + request.getSession().getId());

        for (String m : messages) {
            System.out.println(m);
        }

        return ResponseEntity.ok().body(null);
    }


    @PostMapping(value = "/verifyOTP",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> otp(final HttpServletRequest request,
                                      final int otp) {

        if(otps.getOrDefault(request.getSession().getId(), -1) != otp) {
            return ResponseEntity.badRequest().body("Wrong OTP");
        }
        return ResponseEntity.ok().body("Login Success");
    }

    @PostMapping(value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginUser(final HttpServletRequest request,
                                            final String email,
                                            final String password) {

        User user = userService.findUser(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email");
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
            return ResponseEntity.ok().body("OTP Send");
        }
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Bad password");
        }
        return ResponseEntity.ok().body("Login Success");
    }

    @GetMapping(value = "/authType",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authType(final HttpServletRequest request,
                                           final String email) {

        User user = userService.findUser(email);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email");
        }
        if (user.getType().equalsIgnoreCase("student")) {
            return ResponseEntity.ok().body("OTP");
        }

        return ResponseEntity.ok().body("PASSWORD");
    }
}

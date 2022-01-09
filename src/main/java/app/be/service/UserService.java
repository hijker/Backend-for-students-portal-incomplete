package app.be.service;

import app.be.dao.service.UserDaoService;
import app.be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDaoService userDaoService;

    public UserService() {
    }

    public User insertUser(final String userName,
                           final String password) {
        final User user = new User();
        user.setName(userName);
        user.setEmail(userName);
        user.setPassword(password);
        return userDaoService.createUsers(user);
    }

    public User findUser(String email) {
        return userDaoService.findUser(email);
    }
}

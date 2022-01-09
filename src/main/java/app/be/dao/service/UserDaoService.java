package app.be.dao.service;

import app.be.dao.repository.UsersRepository;
import app.be.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDaoService {

    @Autowired
    private UsersRepository usersRepository;

    public UserDaoService() {
    }

    public User createUsers(final User user) {
        return usersRepository.save(user);
    }

    public User findUser(String email) {
        return usersRepository.findById(email).orElse(null);
    }
}

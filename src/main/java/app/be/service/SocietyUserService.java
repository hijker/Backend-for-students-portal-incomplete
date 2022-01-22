package app.be.service;

import app.be.dao.service.SocietyUserDaoService;
import app.be.model.SocietyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocietyUserService {

    @Autowired
    private SocietyUserDaoService societyUserDaoService;

    public SocietyUserService() {
    }

    public SocietyUser insertUser(final String name,
                                  final String email,
                                  final String password) {
        final SocietyUser user = new SocietyUser();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return societyUserDaoService.createUsers(user);
    }

    public SocietyUser findUser(String email) {
        return societyUserDaoService.findSocietyUser(email);
    }
}

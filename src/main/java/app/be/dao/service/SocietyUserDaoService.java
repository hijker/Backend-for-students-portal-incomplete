package app.be.dao.service;

import app.be.dao.repository.SocietyUsersRepository;
import app.be.model.SocietyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocietyUserDaoService {

    @Autowired
    private SocietyUsersRepository societyUsersRepository;

    public SocietyUserDaoService() {
    }

    public SocietyUser createUsers(final SocietyUser user) {
        return societyUsersRepository.save(user);
    }

    public SocietyUser findSocietyUser(String email) {
        return societyUsersRepository.findById(email).orElse(null);
    }
}

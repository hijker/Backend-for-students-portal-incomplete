package app.be.dao.repository;


import app.be.model.SocietyUser;
import app.be.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SocietyUsersRepository extends MongoRepository<SocietyUser, String> {
}
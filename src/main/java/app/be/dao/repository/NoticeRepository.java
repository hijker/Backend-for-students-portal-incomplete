package app.be.dao.repository;


import app.be.model.Notice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticeRepository extends MongoRepository<Notice, String> {

    Notice findByName(String name);
}
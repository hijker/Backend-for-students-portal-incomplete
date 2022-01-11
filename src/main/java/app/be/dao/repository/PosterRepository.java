package app.be.dao.repository;


import app.be.model.Poster;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PosterRepository extends MongoRepository<Poster, String> {

    Poster findByName(String name);
}
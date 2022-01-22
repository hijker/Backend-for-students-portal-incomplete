package app.be.dao.repository;


import app.be.model.Grievance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GrievanceRepository extends MongoRepository<Grievance, String> {

    List<Grievance> findGrievanceByEmail(String email);
}
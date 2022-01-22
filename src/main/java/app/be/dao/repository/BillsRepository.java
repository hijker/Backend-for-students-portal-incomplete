package app.be.dao.repository;


import app.be.model.Bills;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BillsRepository extends MongoRepository<Bills, String> {

    List<Bills> findBillsByEmail(String email);
}
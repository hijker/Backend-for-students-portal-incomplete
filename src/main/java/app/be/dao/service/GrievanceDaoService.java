package app.be.dao.service;

import app.be.dao.repository.GrievanceRepository;
import app.be.model.Grievance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrievanceDaoService {

    @Autowired
    private GrievanceRepository grievanceRepository;

    public GrievanceDaoService() {
    }

    public Grievance createGrievance(final Grievance grievance) {
        return grievanceRepository.save(grievance);
    }

    public List<Grievance> findByEmail(String email) {
        return grievanceRepository.findGrievanceByEmail(email);
    }
}

package app.be.service;

import app.be.dao.service.GrievanceDaoService;
import app.be.model.Grievance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrievanceService {

    @Autowired
    private GrievanceDaoService grievanceDaoService;

    public GrievanceService() {
    }

    public Grievance insertGrievance(String email,
                                     String overview,
                                     String category,
                                     String description,
                                     String status,
                                     String remark,
                                     String fileName,
                                     byte[] data) {
        final Grievance grievance = new Grievance();
        grievance.setEmail(email);
        grievance.setOverview(overview);
        grievance.setCategory(category);
        grievance.setDescription(description);
        grievance.setStatus(status);
        grievance.setRemark(remark);
        grievance.setFileName(fileName);
        grievance.setCategory(category);
        grievance.setData(data);
        return grievanceDaoService.createGrievance(grievance);
    }

    public List<Grievance> findGrievanceByEmail(String email) {
        return grievanceDaoService.findByEmail(email);
    }
}

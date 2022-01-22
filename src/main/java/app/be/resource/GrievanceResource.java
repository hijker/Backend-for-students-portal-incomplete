package app.be.resource;

import app.be.model.Grievance;
import app.be.response.GrievanceListResponse;
import app.be.service.GrievanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/grievance")
public class GrievanceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GrievanceResource.class);

    @Autowired
    private GrievanceService grievanceService;

    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Grievance> upload(final HttpServletRequest request,
                                            String email,
                                            String overview,
                                            String category,
                                            String description,
                                            String status,
                                            String remark,
                                            @RequestParam("file") MultipartFile file) throws IOException {
        String altName = file.getOriginalFilename();
        Grievance grievance = grievanceService.insertGrievance(email, overview, category, description, status, remark, altName, file.getBytes());

        return ResponseEntity.ok().body(grievance);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GrievanceListResponse> all(final HttpServletRequest request,
                                                     final String email) {
        List<Grievance> grievanceByEmail = grievanceService.findGrievanceByEmail(email);
        return ResponseEntity.ok().body(new GrievanceListResponse(grievanceByEmail.size(), grievanceByEmail));
    }

}

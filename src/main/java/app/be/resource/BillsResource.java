package app.be.resource;

import app.be.model.Bills;
import app.be.response.BillsListResponse;
import app.be.service.BillsService;
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
@RequestMapping("/bill")
public class BillsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillsResource.class);

    @Autowired
    private BillsService billsService;

    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bills> upload(final HttpServletRequest request,
                                        String email,
                                        String description,
                                        String status,
                                        String remark,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        String altName = file.getOriginalFilename();
        Bills Bills = billsService.insertBills(email, description, status, remark, altName, file.getBytes());

        return ResponseEntity.ok().body(Bills);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillsListResponse> all(final HttpServletRequest request,
                                                 final String email) {
        List<Bills> BillsByEmail = billsService.findBillsByEmail(email);
        return ResponseEntity.ok().body(new BillsListResponse(BillsByEmail.size(), BillsByEmail));
    }

}

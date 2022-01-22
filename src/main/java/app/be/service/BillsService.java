package app.be.service;

import app.be.dao.service.BillsDaoService;
import app.be.model.Bills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillsService {

    @Autowired
    private BillsDaoService BillsDaoService;

    public BillsService() {
    }

    public Bills insertBills(String email,
                             String description,
                             String status,
                             String remark,
                             String fileName,
                             byte[] data) {
        final Bills bills = new Bills();
        bills.setEmail(email);
        bills.setDescription(description);
        bills.setStatus(status);
        bills.setRemark(remark);
        bills.setFileName(fileName);
        bills.setData(data);
        return BillsDaoService.createBills(bills);
    }

    public List<Bills> findBillsByEmail(String email) {
        return BillsDaoService.findByEmail(email);
    }
}

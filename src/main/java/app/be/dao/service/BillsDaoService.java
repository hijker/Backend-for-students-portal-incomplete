package app.be.dao.service;

import app.be.dao.repository.BillsRepository;
import app.be.model.Bills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillsDaoService {

    @Autowired
    private BillsRepository billsRepository;

    public BillsDaoService() {
    }

    public Bills createBills(final Bills bills) {
        return billsRepository.save(bills);
    }

    public List<Bills> findByEmail(String email) {
        return billsRepository.findBillsByEmail(email);
    }
}

package app.be.response;

import app.be.model.Bills;

import java.util.List;

public class BillsListResponse {

    final int count;

    final List<Bills> bills;

    public BillsListResponse(int count, List<Bills> bills) {
        this.count = count;
        this.bills = bills;
    }
}

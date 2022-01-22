package app.be.response;

import app.be.model.Grievance;

import java.util.List;

public class GrievanceListResponse {

    final int count;

    final List<Grievance> grievances;

    public GrievanceListResponse(int count, List<Grievance> grievances) {
        this.count = count;
        this.grievances = grievances;
    }
}

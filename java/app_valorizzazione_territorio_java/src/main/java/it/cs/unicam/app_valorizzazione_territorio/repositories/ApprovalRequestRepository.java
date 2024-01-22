package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.requests.ApprovalRequest;

public class ApprovalRequestRepository extends Repository<ApprovalRequest> {
    private static ApprovalRequestRepository instance;

    private ApprovalRequestRepository() {
        super();
    }

    public static ApprovalRequestRepository getInstance() {
        if (instance == null) instance = new ApprovalRequestRepository();
        return instance;
    }
}

package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;

import it.cs.unicam.app_valorizzazione_territorio.builders.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contest.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestApprovalRequest;

public class ContestContentInsertionHandler extends ContentInsertionHandler<Contest> {
    private User user;
    private Contest contest;

    public ContestContentInsertionHandler(long userID, long contestID){
        super(new ContestContentBuilder(MunicipalityRepository.getInstance().getContestByID(contestID),
                UserRepository.getInstance().getItemByID(userID)));

        this.user = UserRepository.getInstance().getItemByID(userID);
        this.contest = MunicipalityRepository.getInstance().getContestByID(contestID);

        if(!contest.permitsUser(user))
            throw new IllegalArgumentException("User cannot insert content in this contest");
    }

    @Override
    public void insertContent() {
        if(super.getContent() == null)
            throw new IllegalStateException("Content must first be created");

        contest.getProposalRequests().proposeContent((ContestContent) super.getContent());
        ApprovalRequestRepository.getInstance().add(new
                ContestApprovalRequest(user, (ContestContent) super.getContent(), contest));
    }

}

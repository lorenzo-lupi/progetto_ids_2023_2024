package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;

import it.cs.unicam.app_valorizzazione_territorio.builders.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.ApprovalRequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.ContestApprovalRequest;

public class ContestContentInsertionHandler extends ContentInsertionHandler<Contest, ContestContent> {
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

    /**
     * Inserts the content obtained from the given contentIF in the contest with the given ID.
     * If the content insertion has success, an approval request for the entertainer of the contest is created.
     *
     * @param userID the ID of the user who is inserting the content
     * @param contestID the ID of the contest to which the content is related
     * @param contentIF the contentIF from which the content will be created
     * @return the ID of the created and inserted content
     */
    public static long insertContent(long userID, long contestID, ContentIF contentIF){
        User user = UserRepository.getInstance().getItemByID(userID);
        Contest contest = MunicipalityRepository.getInstance().getContestByID(contestID);

        ContestContent content = ContentInsertionHandler.createContent(
                new ContestContentBuilder(contest, user), contentIF);

        contest.getProposalRequests().proposeContent(content);
        ApprovalRequestRepository.getInstance().add(new ContestApprovalRequest(user, content, contest));
        return content.getID();
    }

    @Override
    public void insertContent() {
        ContestContent content = super.getContent();
        if(super.getContent() == null)
            throw new IllegalStateException("Content must first be created");

        contest.getProposalRequests().proposeContent(content);
        ApprovalRequestRepository.getInstance().add(new
                ContestApprovalRequest(user, super.getContent(), contest));
    }

}

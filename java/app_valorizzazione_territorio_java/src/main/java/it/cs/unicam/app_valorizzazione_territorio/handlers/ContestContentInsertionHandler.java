package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;

import it.cs.unicam.app_valorizzazione_territorio.builders.ContestContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.RequestRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.requests.RequestFactory;

public class ContestContentInsertionHandler {

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
        RequestRepository.getInstance().add(RequestFactory.getApprovalRequest(content));
        return content.getID();
    }


}

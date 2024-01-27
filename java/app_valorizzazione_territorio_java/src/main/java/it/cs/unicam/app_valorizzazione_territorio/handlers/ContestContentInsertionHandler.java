package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.UserRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchCriterion;

public class ContestContentInsertionHandler extends ContentInsertionHandler {
    User user;
    Contest contest;
    public ContestContentInsertionHandler(long userID, long contestID){
        super(userID, contestID);
        this.user = UserRepository.getInstance().getItemByID(userID);
        this.contest = MunicipalityRepository.getInstance().getContestByID(contestID);
    }

}

package it.cs.unicam.app_valorizzazione_territorio.builders;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;

import it.cs.unicam.app_valorizzazione_territorio.model.User;

public class ContestContentBuilder extends ContentBuilder<Contest, ContestContent>{

    private final Contest contest;

    public ContestContentBuilder(Contest contest, User user){
        super(user);
        if (contest == null)
            throw new IllegalArgumentException("Contest cannot be null");
        this.contest = contest;
    }
    @Override
    public ContestContent build( ){
        return new ContestContent(super.getDescription(), this.contest, super.getFiles(), super.getUser());
    }

}

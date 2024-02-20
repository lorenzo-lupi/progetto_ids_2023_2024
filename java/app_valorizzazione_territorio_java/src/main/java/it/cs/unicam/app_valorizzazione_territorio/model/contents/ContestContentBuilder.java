package it.cs.unicam.app_valorizzazione_territorio.model.contents;

import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;

import it.cs.unicam.app_valorizzazione_territorio.model.User;

public class ContestContentBuilder extends ContentBuilder<Contest, ContestContent>{

    private final Contest contest;

    public ContestContentBuilder(Contest contest){
        super();
        if (contest == null)
            throw new IllegalArgumentException("Contest cannot be null");
        this.contest = contest;
    }
    @Override
    public ContentBuilder<Contest, ContestContent> build( ){
        this.result = new ContestContent(super.getDescription(), this.contest, super.getFiles(), super.getUser());
        return this;
    }

    /**
     * Returns the built contest associated to the content bulder.
     * @return the built contest associated to the content bulder
     */
    public Contest getContest() {
        return contest;
    }
}

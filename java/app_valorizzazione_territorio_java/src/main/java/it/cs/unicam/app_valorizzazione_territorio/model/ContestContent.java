package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.contest.Contest;

import java.io.File;
import java.util.List;

public class ContestContent extends Content<Contest>{
    private final Contest contest;

    /**
     * Constructor for a content.
     *
     * @param description the textual description of the content
     * @param files       the multimedia files of the content
     * @throws IllegalArgumentException if description, pointOfInterest or files are null
     */
    public ContestContent(String description, Contest contest, List<File> files) {
        super(description, files);
        if (contest == null)
            throw new IllegalArgumentException("Contest cannot be null");
        this.contest = contest;

    }

    @Override
    public Contest getHost() {
        return this.contest;
    }
}

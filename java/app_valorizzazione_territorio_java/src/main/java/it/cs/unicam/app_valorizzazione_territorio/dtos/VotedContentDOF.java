package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

/**
 * This class represents a VotedContent Detailed Output Format object.
 *
 * @param contentDOF
 * @param votes
 */
public record VotedContentDOF(ContentDOF contentDOF, int votes) implements Identifiable {
    @Override
    public long getID() {
        return contentDOF.getID();
    }
}

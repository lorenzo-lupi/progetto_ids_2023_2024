package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Identifiable;

/**
 * This class represents a VotedContent Synthesized Output Format object.
 *
 * @param contentSOF
 * @param votes
 */
public record VotedContentSOF(ContentSOF contentSOF, int votes) implements Identifiable {
    @Override
    public long getID() {
        return contentSOF.getID();
    }
}

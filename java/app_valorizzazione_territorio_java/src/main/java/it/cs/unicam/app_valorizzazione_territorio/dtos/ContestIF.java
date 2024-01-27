package it.cs.unicam.app_valorizzazione_territorio.dtos;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;

import java.util.Date;
import java.util.List;

public record ContestIF(String name,
                        long animatorID,
                        String topic,
                        String rules,
                        boolean isPrivate,
                        List<Long> userIDs,
                        Long geoLocatableID,
                        Date startDate,
                        Date votingStartDate,
                        Date endDate) {
}

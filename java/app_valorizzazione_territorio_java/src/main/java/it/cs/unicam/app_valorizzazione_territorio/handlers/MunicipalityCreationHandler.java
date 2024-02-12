package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.MunicipalityBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;

import java.io.File;

/**
 * This class represents a handler for the creation of a municipality.
 */
public class MunicipalityCreationHandler {

    /**
     * Creates a new municipality and adds it to the repository of municipalities.
     *
     * @param municipalityIF the municipality input format
     * @return the ID of the created municipality
     */
    public static long createMunicipality(MunicipalityIF municipalityIF) {
        MunicipalityBuilder builder = new MunicipalityBuilder();
        builder.buildName(municipalityIF.name())
                .buildDescription(municipalityIF.description())
                .buildPosition(municipalityIF.position())
                .buildCoordinatesBox(municipalityIF.coordinatesBox());
        for (File file : municipalityIF.files()) builder.buildFile(file);
        builder.build();

        MunicipalityRepository.getInstance().add(builder.obtainResult());
        return builder.obtainResult().getID();
    }
}

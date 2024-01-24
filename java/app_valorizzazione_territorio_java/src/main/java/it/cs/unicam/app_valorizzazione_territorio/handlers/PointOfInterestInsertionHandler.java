package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.builders.PointOfInterestBuilder;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MapDOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.IdsUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.builders.MapBuilder;
import it.cs.unicam.app_valorizzazione_territorio.osm.Map;
import it.cs.unicam.app_valorizzazione_territorio.osm.MapProvider;

import java.io.IOException;

public class PointOfInterestInsertionHandler {
    private User user;
    private Municipality municipality;
    private PointOfInterestBuilder builder;
    private Map<?> map;


    /**
     * Constructor for a PointOfInterestInsertionHandler.
     *
     * @param userId the ID of the user who is inserting the point of interest
     */
    public PointOfInterestInsertionHandler(long userId, long municipalityId) throws IOException {
        this.user = IdsUtils.getUserObject(userId);
        this.municipality = IdsUtils.getMunicipalityObject(municipalityId);
        this.builder = new PointOfInterestBuilder(municipality);
        this.map = MapProvider.getEmptyMap(municipality);
    }

    //TODO manipola mappa
    public Map<?> getMap() {
        return map;
    }

    //TODO inserisci coordinate

    //TODO inserisci nome

    //TODO inserisci descrizione

    //TODO inserisci immagine

    //TODO rimuovi immagine

    //TODO obtain result

    //TODO insert poi

}

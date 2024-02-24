package it.cs.unicam.app_valorizzazione_territorio.repositories;

import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contest.Contest;
import it.cs.unicam.app_valorizzazione_territorio.exceptions.GeoLocatableNotFoundException;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.CompoundPoint;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.PointOfInterest;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * This singleton class provides a repository for the municipalities of the system.
 * It also provides methods to manage the geo-locatable points of the municipalities and contents
 * of their POIs.
 */
public class MunicipalityRepository extends Repository<Municipality> {
    private static MunicipalityRepository instance;
    private long nextGeoLocalizableID = 0L;
    private long nextContestID = 0L;
    private long nextContentID = 0L;

    private MunicipalityRepository() {
        super();
    }

    public static MunicipalityRepository getInstance() {
        if (instance == null) instance = new MunicipalityRepository();
        return instance;
    }

    /**
     * Returns and updates the next available ID for the geo-localizables of
     * the municipalities in the repository.
     *
     * @return the next available ID for the geo-localizables of the repository
     */
    public long getNextGeoLocalizableID() {
        return nextGeoLocalizableID++;
    }

    /**
     * Returns and updates the next available ID for the contests of the
     * municipalities in the repository.
     *
     * @return the next available ID for the contests of the municipalities in the repository
     */
    public long getNextContestID() { return nextContestID++; }

    /**
     * Returns and updates the next available ID for the content of the
     * contents of the POIs of the municipalities in the repository.
     *
     * @return the next available ID for the contents of the POIs of the municipalities in the repository
     */
    public long getNextContentID() {
        return nextContentID++;
    }


    /**
     * Returns a map of all the geo-locatable points of the municipalities in the repository mapped with their IDs.
     *
     * @return a map of all the geo-locatable points of the municipalities
     */
    public Map<Long, GeoLocatable> getAllGeoLocatablesMap() {
        return this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getGeoLocatables().stream())
                .collect(toMap(GeoLocatable::getID, Function.identity()));
    }

    /**
     * Returns a map of all the contests of the municipalities in the repository mapped with their
     * base contest IDs.
     *
     * @return a map of all the contests of the municipalities
     */
    public Map<Long, Contest> getAllContestsMap() {
        return this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getContests().stream())
                .collect(toMap(Contest::getBaseContestID, Function.identity()));
    }

    /**
     * Returns a map of all the content of the content hosts of the municipalities in the repository
     * mapped with their IDs.
     *
     * @return a map of all the content of the content hosts of the municipalities
     */
    public Map<Long, Content> getAllContentsMap() {
        Map<Long, Content> poiContentsMap = this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getGeoLocatables().stream())
                .filter(geoLocatable -> geoLocatable instanceof PointOfInterest)
                .flatMap(pointOfInterest -> ((PointOfInterest) pointOfInterest).getContents().stream())
                .collect(toMap(PointOfInterestContent::getID, Function.identity()));
        Map<Long, Content> contestContentsMap = this.getItemStream().parallel()
                .flatMap(municipality -> municipality.getContests().stream())
                .flatMap(contest -> contest.getContents().stream())
                .collect(toMap(Content::getID, Function.identity()));

        return Stream.concat(poiContentsMap.entrySet().stream(), contestContentsMap.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Returns the geo-locatable point of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the geo-locatable point to be returned
     * @return the geo-locatable point of the municipalities in the repository with the given ID
     * @throws IllegalArgumentException if the geo-locatable point with the given ID is not found
     */
    public GeoLocatable getGeoLocatableByID(long ID) {
        GeoLocatable geoLocatable = getAllGeoLocatablesMap().get(ID);
        if(geoLocatable == null) throw new GeoLocatableNotFoundException("GeoLocatable not found");
        return geoLocatable;
    }

    /**
     * Returns true if the repository contains a geo-locatable point with the given ID.
     *
     * @param id the ID of the geo-locatable point to be checked
     * @return true if the repository contains a geo-locatable point with the given ID
     */
    public boolean containsGeoLocatable(long id) {
        return getAllGeoLocatablesMap().containsKey(id);
    }

    /**
     * Returns true if the repository contains the given geo-locatable point.
     * @param geoLocatable the geo-locatable point to be checked
     * @return true if the repository contains the given geo-locatable point
     */
    public boolean containsGeoLocatable(GeoLocatable geoLocatable) {
        return getAllGeoLocatablesMap().containsValue(geoLocatable);
    }

    /**
     * Returns the point of interest of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the point of interest to be returned
     * @return the point of interest of the municipalities in the repository with the given ID
     * @throws IllegalArgumentException if the geo-locatable point with the given ID is not found or
     * is not a point of interest
     */
    public PointOfInterest getPointOfInterestByID(long ID) {
        if (!(getGeoLocatableByID(ID) instanceof PointOfInterest pointOfInterest))
            throw new IllegalArgumentException("The given geo-locatable ID does not correspond to a point of interest");
        return pointOfInterest;
    }

    /**
     * Returns the compound point of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the compound point to be returned
     * @return the compound point of the municipalities in the repository with the given ID
     * @throws IllegalArgumentException if the geo-locatable point with the given ID is not found or
     * is not a compound point
     */
    public CompoundPoint getCompoundPointByID(long ID) {
        if (!(getGeoLocatableByID(ID) instanceof CompoundPoint compoundPoint))
            throw new IllegalArgumentException("The given geo-locatable ID does not correspond to a compound point");
        return compoundPoint;
    }

    /**
     * Returns the contest of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the contest to be returned
     * @return the contest of the municipalities in the repository with the given ID
     * @throws IllegalArgumentException if the contest with the given ID is not found
     */
    public Contest getContestByID(long ID) {
        Contest contest = getAllContestsMap().get(ID);
        if(contest == null) throw new IllegalArgumentException("Contest not found");
        return contest;
    }


    /**
     * Returns the content of the content hosts of the municipalities in the repository with the given ID.
     *
     * @param ID the ID of the content to be returned
     * @return the content of the content hosts of the municipalities in the repository with the given ID
     * @throws IllegalArgumentException if the content with the given ID is not found
     */
    public Content getContentByID(long ID) {
        Content content = getAllContentsMap().get(ID);
        if(content == null) throw new IllegalArgumentException("Content not found");
        return content;
    }

    /**
     * Returns true if the repository contains a contest with the given ID.
     * @param id the ID of the contest to be checked
     * @return true if the repository contains a contest with the given ID
     */
    public boolean containsContent(long id) {
        return getAllContentsMap().containsKey(id);
    }

    /**
     * Returns a stream of all the geo-locatable points of the municipalities in the repository.
     *
     * @return a stream of all the geo-locatable points of the municipalities in the repository
     */
    public Stream<GeoLocatable> getAllGeoLocatables() {
        return getAllGeoLocatablesMap().values().stream();
    }

    /**
     * Returns a stream of all the contests of the municipalities in the repository.
     *
     * @return a stream of all the contests of the municipalities in the repository
     */
    public Stream<Contest> getAllContests() {
        return getAllContestsMap().values().stream();
    }


    /**
     * Returns a stream of all the content of the content hosts of the municipalities in the repository.
     *
     * @return a stream of all the content of the content hosts of the municipalities in the repository
     */
    public Stream<Content> getAllContents() {
        return getAllContentsMap().values().stream();
    }

    @Override
    public void clear() {
        super.clear();
        nextGeoLocalizableID = 0L;
        nextContestID = 0L;
        nextContentID = 0L;
    }

    /**
     * Removes a content from the repository.
     * @param content the content to be removed
     * @return true if the content point has been removed, false otherwise
     */
    public boolean removeContent(Content<?> content) {
        return this.getAllContentsMap().remove(content.getID(), content);
    }

    /**
     * Removes geo-locatable point to the repository.
     * @param geoLocatable the geo-locatable point to be added
     * @return true if the geo-locatable point has been added, false otherwise
     */
    public boolean removeGeoLocatable(GeoLocatable geoLocatable){
        return this.getAllGeoLocatablesMap().remove(geoLocatable.getID(), geoLocatable);
    }
}

package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.ContentSOF;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.PointOfInterest;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.util.List;

/**
 * This class represents a handler for the search and visualization of the contents of a point of interest.
 */
public class ContentVisualizationHandler extends SearchHandler<PointOfInterestContent>{
    private final PointOfInterest pointOfInterest;

    /**
     * Creates a new ContentVisualizationHandler for the point of interest corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @throws IllegalArgumentException if the point of interest corresponding to the given ID is not found
     */
    public ContentVisualizationHandler(long pointOfInterestID) {
        super(MunicipalityRepository.getInstance().getPointOfInterestByID(pointOfInterestID).getContents().stream()
                .filter(Content::isApproved)
                .toList()
        );
        this.pointOfInterest = MunicipalityRepository.getInstance().getPointOfInterestByID(pointOfInterestID);
    }

    /**
     * Returns the Synthesized Format of all the approved contents of the point of interest corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public static List<ContentSOF> viewApprovedContents(long pointOfInterestID) {
        return MunicipalityRepository.getInstance().getPointOfInterestByID(pointOfInterestID)
                .getApprovedContents()
                .stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents of the point of interest corresponding to the given ID
     *
     * @param pointOfInterestID the ID of the point of interest
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public static List<ContentSOF> viewAllContents(long pointOfInterestID) {
        return MunicipalityRepository.getInstance().getPointOfInterestByID(pointOfInterestID)
                .getContents()
                .stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the contents of the point of interest corresponding to the given ID
     * that satisfy the given filters, all applied in logical and.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param filters the filters to apply
     * @return the Synthesized Format of all the contest in the point of interest corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public static List<ContentSOF> viewFilteredContents(long pointOfInterestID, List<SearchFilter> filters) {
        return (List<ContentSOF>) getFilteredItems(MunicipalityRepository.getInstance().getAllContents().toList(), filters);
    }

    /**
     * Returns the Detailed Format of a Content corresponding to the given ID in the point of interest
     * corresponding to the given ID.
     *
     * @param pointOfInterestID the ID of the point of interest
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     * @throws IllegalArgumentException if the Content having the given ID is not found
     */
    public static ContentDOF viewContent(long pointOfInterestID, long contentID) {
        return getContent(MunicipalityRepository.getInstance().getPointOfInterestByID(pointOfInterestID), contentID)
                .getDetailedFormat();
    }

    /**
     * Returns the Detailed Format of a Content  in the system corresponding to the given ID.
     * The content can belong to any point of interest.
     *
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     */
    public static ContentDOF viewContentFromRepository(long contentID) {
        return MunicipalityRepository.getInstance().getContentByID(contentID).getDetailedFormat();
    }

    /**
     * Returns the Synthesized Format of all the approved contents of the point of interest.
     *
     * @return the Synthesized Format of all the contents of the point of interest
     */
    public List<ContentSOF> viewApprovedContents() {
        return this.pointOfInterest.getContents().stream()
                .map(Content::getSynthesizedFormat)
                .toList();
    }

    /**
     * Returns the Detailed Format of a Content having the given ID in the point of interest.
     *
     * @param contentID the ID of the Content to visualize
     * @return the Detailed Format of the Content having the given ID
     * @throws IllegalArgumentException if the Content having the given ID is not found
     */
    public ContentDOF viewContent(long contentID) {
        return getContent(this.pointOfInterest, contentID).getDetailedFormat();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentSOF> getSearchResult(){
        return (List<ContentSOF>) super.getSearchResult();
    }

    private static Content<PointOfInterest> getContent(PointOfInterest pointOfInterest, long contentID) {
        return pointOfInterest.getContents().stream()
                .filter(content -> content.getID() == contentID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));
    }
}

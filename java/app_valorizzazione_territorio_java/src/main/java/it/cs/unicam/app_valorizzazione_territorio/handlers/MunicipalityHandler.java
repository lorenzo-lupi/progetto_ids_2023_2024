package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.utils.SearchUtils;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.MunicipalityBuilder;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.MunicipalityJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.repositories.jpa.RoleJpaRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents a handler for the municipalities.
 * It handles the creation the visualization of municipalities.
 */
@Getter
@Service
public class MunicipalityHandler {

    @Value("${fileResources.path}")
    private String filePath;
    private final MunicipalityJpaRepository municipalityRepository;
    private final RoleJpaRepository roleRepository;

    @Autowired
    public MunicipalityHandler(MunicipalityJpaRepository municipalityRepository,
                               RoleJpaRepository roleRepository) {
        this.municipalityRepository = municipalityRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Creates a new municipality and adds it to the repository of municipalities.
     *
     * @param municipalityIF the municipality input format
     * @return the ID of the created municipality
     */
    public long createMunicipality(MunicipalityIF municipalityIF) {
        MunicipalityBuilder builder = new MunicipalityBuilder();
        builder.buildName(municipalityIF.name())
                .buildDescription(municipalityIF.description())
                .buildPosition(municipalityIF.position())
                .buildCoordinatesBox(municipalityIF.coordinatesBox())
                .buildFiles(municipalityIF.files().stream()
                        .map(file -> new File(filePath + file))
                        .toList())
                .build();
        Municipality municipality = municipalityRepository.saveAndFlush(builder.obtainResult());
        Arrays.stream(AuthorizationEnum.values())
                .forEach(auth ->
                        roleRepository.save(new Role(municipality, auth)));
        return municipality.getID();
    }

    /**
     * Returns the Detailed Format of a Municipality having the given ID.
     *
     * @param municipalityID the ID of the Municipality to visualize
     * @return the Detailed Format of the Municipality having the given ID
     * @throws IllegalArgumentException if the Municipality having the given ID is not found
     **/
    public MunicipalityOF viewMunicipality(long municipalityID) {
        Optional<Municipality> municipality = municipalityRepository.getByID(municipalityID);
        if (municipality.isEmpty()) throw new IllegalArgumentException("Municipality not found");
        return municipality.get().getOutputFormat();
    }

    /**
     * Returns the Synthesized Format of all the municipalities registered in the system.
     *
     * @return the Synthesized Format of all the municipalities in the system
     */
    public List<MunicipalityOF> viewAllMunicipalities() {
        return municipalityRepository.findAll()
                .stream()
                .map(Municipality::getOutputFormat)
                .toList();
    }

    /**
     * Returns the set of all the criteria available for the search.
     *
     * @return the set of all the criteria available for the search
     */
    public Set<String> getSearchCriteria() {
        return SearchUtils.getSearchCriteria();
    }

    /**
     * This method returns the municipality search parameters for the user entity.
     *
     * @return the search parameters for the user entity
     */
    public List<String> getSearchParameters() {
        return new Municipality()
                .getParameters()
                .stream()
                .map(Object::toString)
                .toList();
    }

    /**
     * Returns the Synthesized Format of all the municipalities registered in the system
     * corresponding to the given filters, all applied in logical and.
     *
     * @param filters the filters to apply
     * @return the Synthesized Format of all the municipalities in the system corresponding to the given filters
     */
    @SuppressWarnings("unchecked")
    public List<MunicipalityOF> viewFilteredMunicipalities(List<SearchFilter> filters) {
        return (List<MunicipalityOF>) SearchUtils
                .getFilteredItems(municipalityRepository
                                .findAll()
                                .stream()
                                .toList(),
                        filters);
    }
}

package it.cs.unicam.app_valorizzazione_territorio.model.geolocatable;

import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.*;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.repositories.MunicipalityRepository;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class represents a generic geo-localizable object, that is a physical point associated
 * with geographical coordinates.
 * It includes fundamental details such as a name, a textual description and a representative
 * multimedia content.
 */
@Entity
@NoArgsConstructor(force = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class GeoLocatable implements Requestable, Searchable, Positionable, Deletable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    private final User user;
    /**
     * -- GETTER --
     *  Returns the name of the geo-localizable object.
     *
     * @return the name of the geo-localizable object
     */
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    @ElementCollection
    private final List<File> images;
    @Enumerated(EnumType.STRING)
    private ApprovalStatusEnum approvalStatus;
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "municipality_id")
    private final Municipality municipality;

    @Getter
    @Embedded
    private Position position;


    /**
     * Constructor for a geo-localizable object.
     *
     * @param name the name of the geo-localizable object
     * @param description the textual description of the geo-localizable object
     * @param municipality the municipality of the geo-localizable object
     * @param images the representative multimedia content of the geo-localizable object
     * @throws IllegalArgumentException if coordinates, description or images are null
     */
    public GeoLocatable(String name,
                        String description,
                        Municipality municipality,
                        List<File> images,
                        User user){
        if(name == null || description == null)
            throw new IllegalArgumentException("title and description cannot be null");
        if(municipality == null || images == null)
            throw new IllegalArgumentException("Municipality and images must not be null");
        if (user == null)
            throw new IllegalArgumentException("user cannot be null");

        this.name = name;
        this.description = description;
        this.municipality = municipality;
        this.images = images;
        this.user = user;
        this.approvalStatus = ApprovalStatusEnum.PENDING;
    }


    protected void setPosition(Position position){
        this.position = position;
    }

    /**
     * Sets the name of the geo-localizable object.
     *
     * @param name the name to set
     * @throws IllegalArgumentException if name is null
     */
    public void setName (String name) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be null");
        this.name = name;
    }


    /**
     * Sets the description of the geo-localizable object.
     *
     * @param description the description to set
     * @throws IllegalArgumentException if description is null
     */
    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException("description cannot be null");
        this.description = description;
    }

    /**
     * Adds a representative multimedia content to the geo-localizable object.
     *
     * @param image the representative multimedia content to add
     * @return true if the representative multimedia content has been added, false otherwise
     * @throws IllegalArgumentException if image is null
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addImage(File image) {
        if (image == null)
            throw new IllegalArgumentException("image cannot be null");
        return this.images.add(image);
    }



    /**
     * Removes a representative multimedia content from the geo-localizable object.
     *
     * @param image the representative multimedia content to remove
     * @return true if the representative multimedia content has been removed, false otherwise
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean removeImage(File image) {
        return this.images.remove(image);
    }

    @Override
    public boolean isApproved() {
        return this.approvalStatus == ApprovalStatusEnum.APPROVED;
    }

    @Override
    public void reject() {
        this.approvalStatus = ApprovalStatusEnum.REJECTED;
    }

    @Override
    public void approve() {
        this.approvalStatus = ApprovalStatusEnum.APPROVED;
    }

    @Override
    public ApprovalStatusEnum getApprovalStatus() {
        return this.approvalStatus;
    }

    @Override
    public Runnable getDeletionAction() {
        return () -> {this.getMunicipality().removeGeoLocatable(this);
        MunicipalityRepository.getInstance().removeGeoLocatable(this);};
    }

    @Override
    public Map<Parameter, Consumer<Object>> getSettersMapping() {
        return Map.of(Parameter.NAME, toObjectSetter(this::setName, String.class),
                Parameter.DESCRIPTION, toObjectSetter(this::setDescription, String.class),
                Parameter.ADD_FILE, toObjectSetter(this::addImage, File.class),
                Parameter.REMOVE_FILE, toObjectSetter(this::removeImage, File.class));
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(Parameter.NAME, this.getName(),
                Parameter.DESCRIPTION, this.getDescription(),
                Parameter.MUNICIPALITY, this.getMunicipality(),
                Parameter.POSITION, this.getPosition(),
                Parameter.APPROVAL_STATUS, this.getApprovalStatus(),
                Parameter.USERNAME, this.user.getUsername(),
                Parameter.THIS, this);
    }

    @Override
    public long getID() {
        return this.ID;
    }

    @Override
    public GeoLocatableOF getOutputFormat(){
        return new GeoLocatableOF(
                this.getName(),
                this.getClass().getSimpleName(),
                this.getImages().isEmpty() ? null : this.getImages().get(0),
                this.getMunicipality().getOutputFormat(),
                this.getPosition(),
                this.getDescription(),
                this.getImages(),
                this.getID());
    }

    @Override
    public boolean equals(Object obj) {
        return equalsID(obj);
    }

}

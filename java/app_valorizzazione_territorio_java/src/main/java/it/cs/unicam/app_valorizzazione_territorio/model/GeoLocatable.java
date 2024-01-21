package it.cs.unicam.app_valorizzazione_territorio.model;

import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import it.cs.unicam.app_valorizzazione_territorio.search.Searchable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class GeoLocatable implements Approvable, Searchable {
    private final Position rapresentativePosition;
    private String title;
    private String description;
    private final List<File> images;
    private ApprovalStatusENUM approvalStatus;

    public GeoLocatable(Position rapresentativePosition,
                        String title,
                        String description,
                        ApprovalStatusENUM approvalStatus) {

        if(rapresentativePosition == null)
            throw new IllegalArgumentException("rapresentativePosition cannot be null");
        if(title == null || description == null)
            throw new IllegalArgumentException("title and description cannot be null");
        if(approvalStatus == null)
            throw new IllegalArgumentException("approvalStatus cannot be null");

        this.rapresentativePosition = rapresentativePosition;
        this.title = title;
        this.description = description;
        this.images = new LinkedList<>();
        this.approvalStatus = approvalStatus;
    }

    public Position getPosition(){
        return this.rapresentativePosition;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription(){
        return this.description;
    }

    public void setTitle(String title){
        if (title == null)
            throw new IllegalArgumentException("title cannot be null");
        this.title = title;
    }

    public void setDescription(String description){
        if (description == null)
            throw new IllegalArgumentException("description cannot be null");
        this.description = description;
    }

    public List<File> getImages(){
        return this.images;
    }

    public void addImage(File image){
        if (image == null)
            throw new IllegalArgumentException("image cannot be null");
        this.images.add(image);
    }

    @Override
    public boolean isApproved() {
        return this.approvalStatus == ApprovalStatusENUM.APPROVED;
    }

    @Override
    public void reject() {
        this.approvalStatus = ApprovalStatusENUM.REJECTED;
    }

    @Override
    public void approve() {
        this.approvalStatus = ApprovalStatusENUM.APPROVED;
    }

    @Override
    public ApprovalStatusENUM getApprovalStatus() {
        return this.approvalStatus;
    }

    @Override
    public Map<Parameter, Object> getParametersMapping() {
        return Map.of(
                Parameter.POSITION, this.rapresentativePosition,
                Parameter.DESCRIPTION, this.description+" "+this.title,
                Parameter.APPROVAL_STATUS, this.approvalStatus
        );
    }
}

package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityRequestOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * This class represents a request made to a municipality.
 *
 * @param <I> the type of the item of the request.
 */
@Entity
@DiscriminatorValue("Municipality")
@NoArgsConstructor(force = true)
public class MunicipalityRequest<I extends Visualizable> extends Request<I> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "municipality_id")
    private final Municipality municipality;

    /**
     * Constructor for a municipality approval request.
     *
     * @param command the command that represents the request.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityRequest(RequestCommand<I> command, Municipality municipality) {
        super(command);
        this.municipality = municipality;
    }

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param command the command that represents the request.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityRequest(User user, RequestCommand<I> command, Municipality municipality, String message) {
        super(user, command, message);
        this.municipality = municipality;
    }

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param command the command that represents the request.
     * @param date the date of the request.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityRequest(User user, RequestCommand<I> command, Municipality municipality, Date date, String message) {
        super(user, command, date, message);
        this.municipality = municipality;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    @Override
    public boolean canBeApprovedBy(User user) {
        return user.getAuthorizations(municipality).contains(AuthorizationEnum.CURATOR);
    }

    @Override
    public MunicipalityRequestOF getOutputFormat() {
        return new MunicipalityRequestOF(
                this.getSender().getOutputFormat(),
                this.getMunicipality().getName(),
                this.getMunicipality().getOutputFormat(),
                this.getDate(),
                this.getItem().getOutputFormat(),
                this.getID());
    }
}

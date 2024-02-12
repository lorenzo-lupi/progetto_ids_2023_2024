package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.ApprovalStatusEnum;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.contest.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Date;
import java.util.Calendar;

public class MunicipalityRequest<I extends Visualizable> extends Request<I> {

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
        return user.getAuthorizations(municipality).contains(RoleTypeEnum.CURATOR);
    }

    @Override
    public MunicipalityRequestSOF getSynthesizedFormat() {
        return new MunicipalityRequestSOF(this.getSender().getUsername(),
                this.getMunicipality().getName(), this.getDate(), this.getID());
    }

    @Override
    public MunicipalityRequestDOF getDetailedFormat() {
        return new MunicipalityRequestDOF(this.getSender().getSynthesizedFormat(),
                this.getMunicipality().getSynthesizedFormat(), this.getDate(),
                this.getItem().getSynthesizedFormat(), this.getID());
    }


}

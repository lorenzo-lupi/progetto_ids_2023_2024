package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Identifiable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestDOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.MunicipalityRequestSOF;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.model.RoleTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.User;

import java.util.Date;
import java.util.Calendar;

public class MunicipalityApprovalRequest<I extends Approvable & Visualizable> extends ApprovalRequest<I>{

    private final Municipality municipality;

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param approvableItem the item to be approved.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityApprovalRequest(User user, I approvableItem, Municipality municipality) {
        this(user, approvableItem, municipality, Calendar.getInstance().getTime());
    }

    /**
     * Constructor for a municipality approval request.
     *
     * @param user the user who made the request.
     * @param date the date of the request.
     * @param approvableItem the item to be approved.
     * @param municipality the municipality to which the request is addressed.
     */
    public MunicipalityApprovalRequest(User user, I approvableItem, Municipality municipality, Date date) {
        super(user, approvableItem, date);
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
        return new MunicipalityRequestSOF(this.getUser().getUsername(),
                this.getMunicipality().getName(), this.getDate(), this.getID());
    }

    @Override
    public MunicipalityRequestDOF getDetailedFormat() {
        return new MunicipalityRequestDOF(this.getUser().getSynthesizedFormat(),
                this.getMunicipality().getSynthesizedFormat(), this.getDate(),
                this.getApprovableItem().getSynthesizedFormat(), this.getID());
    }
}

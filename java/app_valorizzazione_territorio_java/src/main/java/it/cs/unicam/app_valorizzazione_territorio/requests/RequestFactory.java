package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

public class RequestFactory {

    public static Request<GeoLocatable> getApprovalRequest(GeoLocatable item) {
        return new MunicipalityRequest<>(new ApprovalCommand<>(item), item.getMunicipality());
    }

    public static Request<? extends Content> getApprovalRequest(Content content) {
        if (content instanceof PointOfInterestContent c) {
            return getApprovalRequest(c);
        } else if (content instanceof ContestContent c) {
            return getApprovalRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getApprovalRequest(PointOfInterestContent content) {
        return new MunicipalityRequest<>(new ApprovalCommand<>(content), content.getHost().getMunicipality());
    }

    public static Request<ContestContent> getApprovalRequest(ContestContent content) {
        return new ContestRequest(new ApprovalCommand<>(content));
    }

    public static Request<? extends Content> getModificationRequest(Content item, Parameter parameter, Object value) {
        if (item instanceof PointOfInterestContent c) {
            return getModificationRequest(c, parameter, value);
        } else if (item instanceof ContestContent c) {
            return getModificationRequest(c, parameter, value);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getModificationRequest(PointOfInterestContent item,
                                                                         Parameter parameter,
                                                                         Object value) {
        return new MunicipalityRequest<>(new ModificationCommand<>(item, parameter, value),
                item.getHost().getMunicipality());
    }

    public static Request<ContestContent> getModificationRequest(ContestContent item,
                                                                 Parameter parameter,
                                                                 Object value) {
        return new ContestRequest(new ModificationCommand<>(item, parameter, value));
    }

    public static Request<GeoLocatable> getDeletionRequest(GeoLocatable item) {
        return new MunicipalityRequest<GeoLocatable>(new DeletionCommand<>(item), item.getMunicipality());
    }

    public static Request<? extends Content> getDeletionRequest(Content item) {
        if (item instanceof PointOfInterestContent c) {
            return getDeletionRequest(c);
        } else if (item instanceof ContestContent c) {
            return getDeletionRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getDeletionRequest(PointOfInterestContent item) {
        return new MunicipalityRequest<>(new DeletionCommand<>(item), item.getHost().getMunicipality());
    }

    public static Request<ContestContent> getDeletionRequest(ContestContent item) {
        return new ContestRequest(new DeletionCommand<>(item));
    }
}

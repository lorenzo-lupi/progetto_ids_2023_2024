package it.cs.unicam.app_valorizzazione_territorio.requests;

import it.cs.unicam.app_valorizzazione_territorio.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;

/**
 * This class represents a factory for creating requests, providing a set of methods to create
 * different types of requests for different types of items.
 * It can be used to create requests for approval, modification, deletion of items and promotion of users.
 */
public class RequestFactory {

    public static <T extends Visualizable & Approvable> Request<?> getApprovalRequest(T item) {
        if (item instanceof GeoLocatable g) {
            return getApprovalRequest(g);
        } else if (item instanceof Content<?> c) {
            return getApprovalRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    public static Request<GeoLocatable> getApprovalRequest(GeoLocatable item) {
        return getApprovalRequest(item, "");
    }

    public static Request<GeoLocatable> getApprovalRequest(GeoLocatable item, String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ApprovalCommand<>(item),
                item.getMunicipality(),
                message);
    }

    public static Request<? extends Content<?>> getApprovalRequest(Content<?> content) {
        return getApprovalRequest(content, "");
    }

    public static Request<? extends Content<?>> getApprovalRequest(Content<?> content, String message) {
        if (content instanceof PointOfInterestContent c) {
            return getApprovalRequest(c, message);
        } else if (content instanceof ContestContent c) {
            return getApprovalRequest(c, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getApprovalRequest(PointOfInterestContent content, String message) {
        return new MunicipalityRequest<>(content.getUser(),
                new ApprovalCommand<>(content),
                content.getHost().getMunicipality(),
                message);
    }

    public static Request<ContestContent> getApprovalRequest(ContestContent content, String message) {
        return new ContestRequest(content.getUser(),
                new ApprovalCommand<>(content),
                message);
    }

    public static <T extends Visualizable & Approvable> Request<?> getModificationRequest(T item,
                                                                                         Parameter parameter,
                                                                                         Object value) {
        if (item instanceof GeoLocatable g) {
            return getModificationRequest(g, parameter, value);
        } else if (item instanceof Content<?> c) {
            return getModificationRequest(c, parameter, value);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    public static Request<GeoLocatable> getModificationRequest(GeoLocatable item,
                                                               Parameter parameter,
                                                               Object value) {
        return getModificationRequest(item, parameter, value, "");
    }

    public static Request<GeoLocatable> getModificationRequest(GeoLocatable item,
                                                               Parameter parameter,
                                                               Object value,
                                                               String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ModificationCommand<>(item, parameter, value),
                item.getMunicipality(),
                message);
    }

    public static Request<? extends Content<?>> getModificationRequest(Content<?> item,
                                                                       Parameter parameter,
                                                                       Object value) {
        return getModificationRequest(item, parameter, value, "");
    }

    public static Request<? extends Content<?>> getModificationRequest(Content<?> item,
                                                                       Parameter parameter,
                                                                       Object value,
                                                                       String message) {
        if (item instanceof PointOfInterestContent c) {
            return getModificationRequest(c, parameter, value, message);
        } else if (item instanceof ContestContent c) {
            return getModificationRequest(c, parameter, value, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getModificationRequest(PointOfInterestContent item,
                                                                         Parameter parameter,
                                                                         Object value,
                                                                         String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ModificationCommand<>(item, parameter, value),
                item.getHost().getMunicipality(),
                message);
    }

    public static Request<ContestContent> getModificationRequest(ContestContent item,
                                                                 Parameter parameter,
                                                                 Object value,
                                                                 String message) {
        return new ContestRequest(item.getUser(),
                new ModificationCommand<>(item, parameter, value),
                message);
    }

    public static <T extends Visualizable & Approvable> Request<?> getDeletionRequest(T item) {
        if (item instanceof GeoLocatable g) {
            return getDeletionRequest(g);
        } else if (item instanceof Content<?> c) {
            return getDeletionRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    public static Request<GeoLocatable> getDeletionRequest(GeoLocatable item) {
        return getDeletionRequest(null, item, "");
    }

    public static Request<GeoLocatable> getDeletionRequest(User user, GeoLocatable item, String message) {
        return new MunicipalityRequest<>(user,
                new DeletionCommand<>(item),
                item.getMunicipality(),
                message);
    }

    public static Request<? extends Content<?>> getDeletionRequest(Content<?> item) {
        return getDeletionRequest(null, item, "");
    }

    public static Request<? extends Content<?>> getDeletionRequest(User user, Content<?> item, String message) {
        if (item instanceof PointOfInterestContent c) {
            return getDeletionRequest(user, c, message);
        } else if (item instanceof ContestContent c) {
            return getDeletionRequest(user, c, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    public static Request<PointOfInterestContent> getDeletionRequest(User user, PointOfInterestContent item, String message) {
        return new MunicipalityRequest<>(user,
                new DeletionCommand<>(item),
                item.getHost().getMunicipality(),
                message);
    }

    public static Request<ContestContent> getDeletionRequest(User user, ContestContent item, String message) {
        return new ContestRequest(user, new DeletionCommand<>(item), message);
    }

    public static Request<User> getPromotionRequest(User user, Role role) {
        return getPromotionRequest(user, role, "");
    }

    public static Request<User> getPromotionRequest(User user, Role role, String message) {
        return new MunicipalityRequest<>(user,
                new ModificationCommand<>(user, Parameter.ADD_ROLE, role),
                role.municipality(),
                message);
    }
}

package it.cs.unicam.app_valorizzazione_territorio.model.requests;

import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Approvable;
import it.cs.unicam.app_valorizzazione_territorio.model.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.ContestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.contents.PointOfInterestContent;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Role;
import it.cs.unicam.app_valorizzazione_territorio.model.User;
import it.cs.unicam.app_valorizzazione_territorio.search.Parameter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * This class represents a factory for creating requests, providing a set of methods to create
 * different types of requests for different types of items.
 * It can be used to create requests for approval, modification, deletion of items and promotion of users.
 */
public class RequestFactory {

    /**
     * Returns a request for the approval of an approvable item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param item the item to be approved
     * @return the request for the approval of the item
     * @param <T> the type of the item to be approved
     */
    public static <T extends Visualizable & Approvable> Request<?> getApprovalRequest(T item) {
        if (item instanceof GeoLocatable g) {
            return getApprovalRequest(g);
        } else if (item instanceof Content<?> c) {
            return getApprovalRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    /**
     * Returns a request for the approval of a geo-locatable item.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the geo-locatable item to be approved
     * @return the request for the approval of the item
     */
    public static Request<GeoLocatable> getApprovalRequest(GeoLocatable item) {
        return getApprovalRequest(item, "");
    }

    /**
     * Returns a request for the approval of a geo-locatable item with the given request message.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the geo-locatable item to be approved
     * @param message the message of the request
     * @return the request for the approval of the item
     */
    public static Request<GeoLocatable> getApprovalRequest(GeoLocatable item, String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ApprovalCommand<>(item),
                item.getMunicipality(),
                message);
    }

    /**
     * Returns a request for the approval of a content item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param content the content item to be approved
     * @return the request for the approval of the item
     */
    public static Request<? extends Content<?>> getApprovalRequest(Content<?> content) {
        return getApprovalRequest(content, "");
    }

    /**
     * Returns a request for the approval of a content item with the given request message.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param content the content item to be approved
     * @param message the message of the request
     * @return the request for the approval of the item
     */
    public static Request<? extends Content<?>> getApprovalRequest(Content<?> content, String message) {
        if (content instanceof PointOfInterestContent c) {
            return getApprovalRequest(c, message);
        } else if (content instanceof ContestContent c) {
            return getApprovalRequest(c, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    /**
     * Returns a request for the approval of a point of interest content item with the given request message.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param content the point of interest content item to be approved
     * @param message the message of the request
     * @return the request for the approval of the item
     */
    public static Request<PointOfInterestContent> getApprovalRequest(PointOfInterestContent content, String message) {
        return new MunicipalityRequest<>(content.getUser(),
                new ApprovalCommand<>(content),
                content.getHost().getMunicipality(),
                message);
    }

    /**
     * Returns a request for the approval of a contest content item with the given request message.
     * The request is addressed to the contest of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param content the contest content item to be approved
     * @param message the message of the request
     * @return the request for the approval of the item
     */
    public static Request<ContestContent> getApprovalRequest(ContestContent content, String message) {
        return new ContestRequest(content.getUser(),
                new ApprovalCommand<>(content),
                message);
    }

    /**
     * Returns a request for the modification of an item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param item the item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @return the request for the modification of the item
     * @param <T> the type of the item to be modified
     */
    public static <T extends Visualizable & Approvable> Request<?> getModificationRequest(T item,
                                                                                          List<Pair<Parameter, Object>> pairs) {
        if (item instanceof GeoLocatable g) {
            return getModificationRequest(g, pairs);
        } else if (item instanceof Content<?> c) {
            return getModificationRequest(c, pairs);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    /**
     * Returns a request for the modification of a geo-locatable item.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the geo-locatable item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @return the request for the modification of the item
     */
    public static Request<GeoLocatable> getModificationRequest(GeoLocatable item,
                                                               List<Pair<Parameter, Object>> pairs) {
        return getModificationRequest(item, pairs, "");
    }

    /**
     * Returns a request for the modification of a geo-locatable item with the given request message.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the geo-locatable item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @param message the message of the request
     * @return the request for the modification of the item
     */
    public static Request<GeoLocatable> getModificationRequest(GeoLocatable item,
                                                               List<Pair<Parameter, Object>> pairs,
                                                               String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ModificationCommand<>(item, pairs),
                item.getMunicipality(),
                message);
    }


    /**
     * Returns a request for the modification of a content item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param item the content item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @return the request for the modification of the item
     */
    public static Request<? extends Content<?>> getModificationRequest(Content<?> item,
                                                                       List<Pair<Parameter, Object>> pairs) {
        return getModificationRequest(item, pairs,  "");
    }



    /**
     * Returns a request for the modification of a content item with the given request message.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param item the content item to be modified
     *  @param pairs the pairs of parameter and value to be modified
     * @param message the message of the request
     * @return the request for the modification of the item
     */
    public static Request<? extends Content<?>> getModificationRequest(Content<?> item,
                                                                       List<Pair<Parameter, Object>> pairs,
                                                                       String message) {
        if (item instanceof PointOfInterestContent c) {
            return getModificationRequest(c, pairs, message);
        } else if (item instanceof ContestContent c) {
            return getModificationRequest(c, pairs, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }



    /**
     * Returns a request for the modification of a point of interest content item with the given request message.
     * The request is addressed to the municipality of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the point of interest content item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @param message the message of the request
     * @return the request for the modification of the item
     */
    public static Request<PointOfInterestContent> getModificationRequest(PointOfInterestContent item,
                                                                         List<Pair<Parameter, Object>> pairs,
                                                                         String message) {
        return new MunicipalityRequest<>(item.getUser(),
                new ModificationCommand<>(item, pairs),
                item.getHost().getMunicipality(),
                message);
    }

    /**
     * Returns a request for the modification of a contest content item with the given request message.
     * The request is addressed to the contest of the item, and the sender is automatically
     * set to the user who made the item.
     *
     * @param item the contest content item to be modified
     * @param pairs the pairs of parameter and value to be modified
     * @param message the message of the request
     * @return the request for the modification of the item
     */
    public static Request<ContestContent> getModificationRequest(ContestContent item,
                                                                 List<Pair<Parameter, Object>> pairs,
                                                                 String message) {
        return new ContestRequest(item.getUser(),
                new ModificationCommand<>(item, pairs),
                message);
    }

    /**
     * Returns a request for the deletion of an item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is automatically set to the user who made the item.
     *
     * @param item the item to be deleted
     * @return the request for the deletion of the item
     * @param <T> the type of the item to be deleted
     */
    public static <T extends Visualizable & Approvable> Request<?> getDeletionRequest(T item) {
        if (item instanceof GeoLocatable g) {
            return getDeletionRequest(g);
        } else if (item instanceof Content<?> c) {
            return getDeletionRequest(c);
        }
        else throw new IllegalArgumentException("Unsupported item type");
    }

    /**
     * Returns a request for the deletion of a geo-locatable item.
     * The request is addressed to the municipality of the item, and the sender is not set.
     *
     * @param item the geo-locatable item to be deleted
     * @return the request for the deletion of the item
     */
    public static Request<GeoLocatable> getDeletionRequest(GeoLocatable item) {
        return getDeletionRequest(null, item, "");
    }

    /**
     * Returns a request for the deletion of a geo-locatable item.
     * The request is addressed to the municipality of the item, and the sender is not set.
     *
     * @param item the geo-locatable item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<GeoLocatable> getDeletionRequest(GeoLocatable item, String message) {
        return getDeletionRequest(null, item, message);
    }

    /**
     * Returns a request for the deletion of a geo-locatable item from the given user with the given request message.
     * The request is addressed to the municipality of the item.
     *
     * @param user the user who made the request
     * @param item the geo-locatable item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<GeoLocatable> getDeletionRequest(User user, GeoLocatable item, String message) {
        return new MunicipalityRequest<>(user,
                new DeletionCommand<>(item),
                item.getMunicipality(),
                message);
    }

    /**
     * Returns a request for the deletion of a content item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is not set.
     *
     * @param item the content item to be deleted
     * @return the request for the deletion of the item
     */
    public static Request<? extends Content<?>> getDeletionRequest(Content<?> item) {
        return getDeletionRequest(null, item, "");
    }

    /**
     * Returns a request for the deletion of a content item.
     * The request is addressed to the municipality or the contest of the item, depending on its type,
     * and the sender is not set.
     *
     * @param item the content item to be deleted
     * @return the request for the deletion of the item
     */
    public static Request<? extends Content<?>> getDeletionRequest(Content<?> item, String message) {
        return getDeletionRequest(null, item, message);
    }

    /**
     * Returns a request for the deletion of a content item from the given user with the given request message.
     * The request is addressed to the municipality or the contest of the item, depending on its type.
     *
     * @param user the user who made the request
     * @param item the content item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<? extends Content<?>> getDeletionRequest(User user, Content<?> item, String message) {
        if (item instanceof PointOfInterestContent c) {
            return getDeletionRequest(user, c, message);
        } else if (item instanceof ContestContent c) {
            return getDeletionRequest(user, c, message);
        }
        else throw new IllegalArgumentException("Unsupported content type");
    }

    /**
     * Returns a request for the deletion of a point of interest content item from the given user
     * with the given request message.
     * The request is addressed to the municipality of the item.

     * @param item the point of interest content item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<PointOfInterestContent> getDeletionRequest(PointOfInterestContent item, String message) {
        return getDeletionRequest(null, item, message);
    }

    /**
     * Returns a request for the deletion of a point of interest content item from the given user
     * with the given request message.
     * The request is addressed to the municipality of the item.
     *
     * @param user the user who made the request
     * @param item the point of interest content item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<PointOfInterestContent> getDeletionRequest(User user, PointOfInterestContent item, String message) {
        return new MunicipalityRequest<>(user,
                new DeletionCommand<>(item),
                item.getHost().getMunicipality(),
                message);
    }

    /**
     * Returns a request for the deletion of a contest content item from the given user with the given request message.
     * The request is addressed to the contest of the item.
     *
     * @param user the user who made the request
     * @param item the contest content item to be deleted
     * @param message the message of the request
     * @return the request for the deletion of the item
     */
    public static Request<ContestContent> getDeletionRequest(User user, ContestContent item, String message) {
        return new ContestRequest(user, new DeletionCommand<>(item), message);
    }


    /**
     * Returns a request for the promotion of a user to a role.
     * The request is addressed to the municipality of the role, and the sender is automatically set to the user
     * receiving the promotion.
     *
     * @param user the user to be promoted
     * @param role the role to which the user is promoted
     * @return the request for the promotion of the user
     */
    public static Request<User> getPromotionRequest(User user, Role role) {
        return getPromotionRequest(user, role, "");
    }

    /**
     * Returns a request for the promotion of a user to a role with the given request message.
     * The request is addressed to the municipality of the role, and the sender is automatically set to the user
     * receiving the promotion.
     *
     * @param user the user to be promoted
     * @param role the role to which the user is promoted
     * @param message the message of the request
     * @return the request for the promotion of the user
     */
    public static Request<User> getPromotionRequest(User user, Role role, String message) {
        return new MunicipalityRequest<>(user,
                new ModificationCommand<>(user, Parameter.ADD_ROLE, role),
                role.municipality(),
                message);
    }
}

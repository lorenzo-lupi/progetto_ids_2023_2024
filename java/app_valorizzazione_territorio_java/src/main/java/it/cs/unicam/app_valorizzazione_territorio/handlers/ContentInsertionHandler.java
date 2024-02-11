package it.cs.unicam.app_valorizzazione_territorio.handlers;

import it.cs.unicam.app_valorizzazione_territorio.contest.ContentHost;
import it.cs.unicam.app_valorizzazione_territorio.abstractions.Visualizable;
import it.cs.unicam.app_valorizzazione_territorio.builders.ContentBuilder;
import it.cs.unicam.app_valorizzazione_territorio.contents.Content;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;

import java.io.File;
import java.util.List;

/**
 * This class represents a handler for the insertion of a content.
 *
 * @param <V> the type of the content host
 * @param <K> the type of the content that will be hosted
 */
public class ContentInsertionHandler<V extends ContentHost<V> & Visualizable,
        K extends Content<V>> {

    /**
     * Creates a content from the specified contentIF.
     *
     * @param builder the builder for the content to be created
     * @param contentIF the contentIF from which the content will be created
     * @return the created content
     * @param <V> the type of the content host
     * @param <K> the type of the content that will be hosted
     * @throws IllegalArgumentException if the builder or the contentIF are null
     */
    public static <V extends ContentHost<V> & Visualizable, K extends Content<V>>
    K createContent(ContentBuilder<V, K> builder, ContentIF contentIF){
        if(builder == null)
            throw new IllegalArgumentException("Builder cannot be null");
        if(contentIF == null)
            throw new IllegalArgumentException("ContentIF cannot be null");

        builder.buildDescription(contentIF.description());
        contentIF.files().forEach(builder::buildFile);
        return builder.build();
    }


}

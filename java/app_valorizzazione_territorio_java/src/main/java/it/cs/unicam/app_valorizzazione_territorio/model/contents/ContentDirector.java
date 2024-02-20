package it.cs.unicam.app_valorizzazione_territorio.model.contents;

/**
 * This class represents a content director that directs the construction of a content.
 * It is useful to create a content from another content of a different concrete type.
 */
public class ContentDirector {
    private ContentBuilder<?,?> builder;

    public ContentDirector(ContentBuilder<?,?> builder){
        this.builder = builder;
    }

    public void setBuilder(ContentBuilder<?,?> builder){
        this.builder = builder;
    }

    /**
     * Makes a content from another content using the specific implementation of the builder given
     * to this director.
     * The new content will have the same user, description and files of the given content and
     * will be available directly with the getResult method of the builder.
     *
     * @param content the content to be used to make the new content
     */
    public void makeFrom(Content<?> content){
        builder.reset().buildUser(content.getUser()).buildDescription(content.getDescription());
        for (var file : content.getFiles()) builder.buildFile(file);
        builder.build();
    }
}

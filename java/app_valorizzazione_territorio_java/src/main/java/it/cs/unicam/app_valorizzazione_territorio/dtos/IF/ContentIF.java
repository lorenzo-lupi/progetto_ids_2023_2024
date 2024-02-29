package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import java.io.File;
import java.util.List;

/**
 * This class represents a Content Input Format object.
 *
 * @param description
 * @param files
 */
public record ContentIF(String description,
                        List<String> files) {
}

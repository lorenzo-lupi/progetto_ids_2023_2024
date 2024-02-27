package it.cs.unicam.app_valorizzazione_territorio.dtos.IF;

import java.io.File;
import java.util.List;

public record CompoundPointIF(String title,
                              String description,
                              String compoundPointType,
                              List<Long> pointsOfInterestIDs,
                              List<String> files) {


}

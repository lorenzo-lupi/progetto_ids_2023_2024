package it.cs.unicam.app_valorizzazione_territorio.model;

import java.util.Map;

public interface Searchable {
    Map<ParameterType, Object> getParameters();
}

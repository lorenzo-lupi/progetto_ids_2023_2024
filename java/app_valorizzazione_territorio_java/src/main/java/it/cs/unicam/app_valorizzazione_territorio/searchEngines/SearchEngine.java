package it.cs.unicam.app_valorizzazione_territorio.searchEngines;

import it.cs.unicam.app_valorizzazione_territorio.model.ParameterType;
import it.cs.unicam.app_valorizzazione_territorio.model.Searchable;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

//TODO: implement
public class SearchEngine<T extends Searchable> {
    private Map<ParameterType, List<BiPredicate<?, ?>>> parameters;


}

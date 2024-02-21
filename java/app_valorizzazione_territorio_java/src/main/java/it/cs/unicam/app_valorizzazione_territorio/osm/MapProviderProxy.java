package it.cs.unicam.app_valorizzazione_territorio.osm;

import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.GeoLocatable;
import it.cs.unicam.app_valorizzazione_territorio.model.Municipality;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapProviderProxy implements MapProvider{

    MapProvider mapProvider;

    private static final java.util.Map<Municipality, Map<?>> emptyMapsCache = new HashMap<>();

    private static final java.util.Map<Municipality, Map<GeoLocatable>> mapsCache = new HashMap<>();

    private static Map<Municipality> municipalitiesMap = null;

    public MapProviderProxy(MapProvider mapProvider) {
        this.mapProvider = mapProvider;
    }

    @Override
    public Map<?> getEmptyMap(Municipality municipality) throws IOException {
        if (emptyMapsCache.containsKey(municipality)) {
            return new Map<>(emptyMapsCache.get(municipality).getOsmData(), new ArrayList<>());
        }
        else {
            Map<?> emptyMap = mapProvider.getEmptyMap(municipality);
            emptyMapsCache.put(municipality, new Map<>(emptyMap.getOsmData(), new ArrayList<>()));
            return emptyMap;
        }
    }

    @Override
    public Map<?> getEmptyMap(CoordinatesBox box) throws IOException {
        return mapProvider.getEmptyMap(box);
    }

    @Override
    public Map<GeoLocatable> getMap(Municipality municipality) throws IOException {
        if (mapsCache.containsKey(municipality)) {
            return new Map<>(mapsCache.get(municipality).getOsmData(), mapsCache.get(municipality).getPointsList());
        }
        else {
            Map<GeoLocatable> map = mapProvider.getMap(municipality);
            mapsCache.put(municipality, new Map(map.getOsmData(), map.getPointsList()));
            return map;
        }
    }

    @Override
    public Map<GeoLocatable> getMap(Municipality municipality, CoordinatesBox box) throws IOException {
        return mapProvider.getMap(municipality, box);
    }

    @Override
    public Map<Municipality> getMunicipalitiesMap() throws IOException {
        if (municipalitiesMap != null) {
            return new Map<>(municipalitiesMap.getOsmData(), municipalitiesMap.getPointsList());
        }
        else {
            Map<Municipality> map = mapProvider.getMunicipalitiesMap();
            municipalitiesMap = new Map<>(map.getOsmData(), map.getPointsList());
            return map;
        }
    }

    @Override
    public Map<GeoLocatable> getFilteredMap(Municipality municipality, List<SearchFilter> filters) throws IOException {
        return mapProvider.getFilteredMap(municipality, filters);
    }

    @Override
    public Map<GeoLocatable> getFilteredMap(Municipality municipality, CoordinatesBox box, List<SearchFilter> filters) throws IOException {
        return mapProvider.getFilteredMap(municipality, box, filters);
    }

    @Override
    public Municipality getMunicipalityByPosition(Position position) throws IOException {
        return mapProvider.getMunicipalityByPosition(position);
    }
}

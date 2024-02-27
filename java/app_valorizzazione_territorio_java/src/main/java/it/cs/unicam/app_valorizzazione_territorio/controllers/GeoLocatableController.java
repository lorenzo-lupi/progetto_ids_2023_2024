package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.CompoundPointIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.PointOfInterestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MapOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.GeoLocatableHandler;
import it.cs.unicam.app_valorizzazione_territorio.osm.utils.PositionParser;
import it.cs.unicam.app_valorizzazione_territorio.osm.CoordinatesBox;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class GeoLocatableController {


    private final GeoLocatableHandler geoLocatableHandler;

    @Autowired
    public GeoLocatableController(GeoLocatableHandler geoLocatableHandler) {
        this.geoLocatableHandler = geoLocatableHandler;
    }

    @JsonView(View.Detailed.class)
    @PostMapping("/visualizeFilteredMap/{municipalityID}/{upperLeftPosition}/{lowerRightPosition}")
    public ResponseEntity<MapOF> visualizeFilteredMap(@PathVariable long municipalityID,
                                                      @PathVariable String upperLeftPosition,
                                                      @PathVariable String lowerRightPosition,
                                                      @RequestBody List<SearchFilter> filters) {
        try {
            CoordinatesBox coordinatesBox = new CoordinatesBox(PositionParser.parse(upperLeftPosition),
                    PositionParser.parse(lowerRightPosition));
            MapOF map = geoLocatableHandler.visualizeFilteredMap(municipalityID, coordinatesBox, filters);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Synthesized.class)
    @PostMapping("viewFilteredGeoLocatables/{municipalityID}")
    public ResponseEntity<List<GeoLocatableOF>> searchFilteredGeoLocatables(@PathVariable long municipalityID,
                                                                            @RequestBody List<SearchFilter> filters) {
        try {
            List<GeoLocatableOF> geoLocatableOFs = geoLocatableHandler
                    .searchFilteredGeoLocatables(municipalityID, filters);
            return new ResponseEntity<>(geoLocatableOFs, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Detailed.class)
    @GetMapping("visualizedDetailedCompoundPoint/{geoLocatableID}")
    public ResponseEntity<GeoLocatableOF> visualizeDetailedCompoundPoint(@PathVariable long geoLocatableID) {
        try {
            return new ResponseEntity<>(geoLocatableHandler.visualizeDetailedCompoundPoint(geoLocatableID), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Detailed.class)
    @GetMapping("visualizeDetailedPointOfInterest/{geoLocatableID}")
    public ResponseEntity<GeoLocatableOF> visualizeDetailedPointOfInterest(@PathVariable long geoLocatableID) {
        try {
            return new ResponseEntity<>(geoLocatableHandler.visualizeDetailedPointOfInterest(geoLocatableID), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("isPositionInMunicipality/{municipalityID}/{position}")
    public ResponseEntity<String> isPositionInMunicipality(@PathVariable long municipalityID,
                                                            @PathVariable String position) {
        try {
            return new ResponseEntity<>("Response value:" + geoLocatableHandler.isPositionInMunicipality(municipalityID,
                    PositionParser.parse(position)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("getSearchCriteria")
    public ResponseEntity<Set<String>> getSearchCriteria() {
        try {
            return new ResponseEntity<>(geoLocatableHandler.getSearchCriteria(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getParameters")
    public ResponseEntity<List<String>> getParameters() {
        try {
            return new ResponseEntity<>(geoLocatableHandler.getSearchParameters(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insertPointOfInterest/{userID}")
    public ResponseEntity<Long> insertPointOfInterest(@PathVariable long userID,
                                                      @RequestBody PointOfInterestIF pointOfInterestIF) {
        try {
            long id = geoLocatableHandler.insertPointOfInterest(userID, pointOfInterestIF);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Synthesized.class)
    @PostMapping("/getFilteredPointOfInterests/{municipalityID}")
    public ResponseEntity<List<GeoLocatableOF>> getFilteredPointOfInterests(@PathVariable long municipalityID,
                                                                            @RequestBody List<SearchFilter> filters) {
        try {
            List<GeoLocatableOF> geoLocatableOFs = geoLocatableHandler.getFilteredPointOfInterests(municipalityID, filters);
            return new ResponseEntity<>(geoLocatableOFs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/obtainPointOfInterestSearchParameters")
    public ResponseEntity<List<String>> obtainPointOfInterestSearchParameters() {
        try {
            List<String> parameters = GeoLocatableHandler.obtainPointOfInterestSearchParameters();
            return new ResponseEntity<>(parameters, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insertCompoundPoint/{municipalityID}/{userID}")
    public ResponseEntity<Long> insertCompoundPoint(@PathVariable long municipalityID,
                                                    @PathVariable long userID,
                                                    @RequestBody CompoundPointIF compoundPointIF) {
        try {
            long id = geoLocatableHandler.insertCompoundPoint(municipalityID, userID, compoundPointIF);
            return new ResponseEntity<>(id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

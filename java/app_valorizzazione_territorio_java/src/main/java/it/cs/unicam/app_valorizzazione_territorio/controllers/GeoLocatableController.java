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
@RequestMapping("geoLocatable")
public class GeoLocatableController {
    private final GeoLocatableHandler geoLocatableHandler;

    @Autowired
    public GeoLocatableController(GeoLocatableHandler geoLocatableHandler) {
        this.geoLocatableHandler = geoLocatableHandler;
    }

    @JsonView(View.Detailed.class)
    @GetMapping("/viewMap/initial/{municipalityID}")
    public ResponseEntity<Object> viewInitialMap(@PathVariable long municipalityID) {
        try {
            MapOF map = geoLocatableHandler.visualizeInitialMap(municipalityID);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @JsonView(View.Detailed.class)
    @PostMapping("/viewMap")
    public ResponseEntity<Object> viewMap(@RequestParam long municipalityID,
                                          @RequestParam String upperLeftPosition,
                                          @RequestParam String lowerRightPosition) {
        try {
            MapOF map = geoLocatableHandler.visualizeMap(municipalityID,
                    new CoordinatesBox(PositionParser.parse(upperLeftPosition), PositionParser.parse(lowerRightPosition)));
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @PostMapping("/viewMap/filtered")
    public ResponseEntity<Object> viewFilteredMap(@RequestParam long municipalityID,
                                                  @RequestParam String upperLeftPosition,
                                                  @RequestParam String lowerRightPosition,
                                                  @RequestBody List<SearchFilter> filters) {
        try {
            CoordinatesBox coordinatesBox = new CoordinatesBox(PositionParser.parse(upperLeftPosition),
                    PositionParser.parse(lowerRightPosition));
            MapOF map = geoLocatableHandler.visualizeFilteredMap(municipalityID, coordinatesBox, filters);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("/viewMap/empty/{municipalityID}")
    public ResponseEntity<Object> viewEmptyMap(@PathVariable long municipalityID) {
        try {
            MapOF map = geoLocatableHandler.getEmptyMap(municipalityID);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("criteria")
    public ResponseEntity<Set<String>> getGeoLocatableSearchCriteria() {
        return new ResponseEntity<>(geoLocatableHandler.getSearchCriteria(), HttpStatus.OK);
    }

    @GetMapping("parameters")
    public ResponseEntity<List<String>> getGeoLocatableParameters() {
        return new ResponseEntity<>(geoLocatableHandler.getSearchParameters(), HttpStatus.OK);
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("searchGeoLocatables")
    public ResponseEntity<Object> searchFilteredGeoLocatables(@RequestParam long municipalityID,
                                                              @RequestBody List<SearchFilter> filters) {
        try {
            List<GeoLocatableOF> geoLocatableOFs = geoLocatableHandler
                    .searchFilteredGeoLocatables(municipalityID, filters);
            return new ResponseEntity<>(geoLocatableOFs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/{geoLocatableID}")
    public ResponseEntity<Object> viewGeoLocatable(@PathVariable long geoLocatableID) {
        try {
            return new ResponseEntity<>(geoLocatableHandler.visualizeDetailedGeoLocatable(geoLocatableID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/compoundPoint/{geoLocatableID}")
    public ResponseEntity<Object> visualizeDetailedCompoundPoint(@PathVariable long geoLocatableID) {
        try {
            return new ResponseEntity<>(geoLocatableHandler.visualizeDetailedCompoundPoint(geoLocatableID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Detailed.class)
    @GetMapping("view/poi/{geoLocatableID}")
    public ResponseEntity<Object> visualizeDetailedPointOfInterest(@PathVariable long geoLocatableID) {
        try {
            return new ResponseEntity<>(geoLocatableHandler.visualizeDetailedPointOfInterest(geoLocatableID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("isPositionInMunicipality")
    public ResponseEntity<String> isPositionInMunicipality(@RequestParam long municipalityID,
                                                           @RequestParam String position) {
        try {
            return new ResponseEntity<>("Response value: " + geoLocatableHandler.isPositionInMunicipality(municipalityID,
                    PositionParser.parse(position)), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/insert/poi/{userID}")
    public ResponseEntity<Object> insertPointOfInterest(@PathVariable long userID,
                                                        @RequestBody PointOfInterestIF pointOfInterestIF) {
        try {
            long id = geoLocatableHandler.insertPointOfInterest(userID, pointOfInterestIF);
            return new ResponseEntity<>("Point of interest created with ID: "+id, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/parameters/poi")
    public ResponseEntity<List<String>> obtainPointOfInterestSearchParameters() {
        List<String> parameters = GeoLocatableHandler.obtainPointOfInterestSearchParameters();
        return new ResponseEntity<>(parameters, HttpStatus.OK);
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("/search/poi/{municipalityID}")
    public ResponseEntity<Object> getFilteredPointOfInterests(@PathVariable long municipalityID,
                                                              @RequestBody List<SearchFilter> filters) {
        try {
            List<GeoLocatableOF> geoLocatableOFs = geoLocatableHandler.getFilteredPointOfInterests(municipalityID, filters);
            return new ResponseEntity<>(geoLocatableOFs, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/insert/compoundPoint/{municipalityID}/{userID}")
    public ResponseEntity<Object> insertCompoundPoint(@RequestParam long municipalityID,
                                                      @RequestParam long userID,
                                                      @RequestBody CompoundPointIF compoundPointIF) {
        try {
            long id = geoLocatableHandler.insertCompoundPoint(municipalityID, userID, compoundPointIF);
            return new ResponseEntity<>("Compound point created with ID: "+id, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

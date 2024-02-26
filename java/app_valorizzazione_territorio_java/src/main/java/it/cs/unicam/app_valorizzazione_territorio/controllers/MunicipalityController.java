package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.MunicipalityIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.MunicipalityHandler;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("municipality")
public class MunicipalityController {

    @Autowired
    MunicipalityHandler municipalityHandler;

    @PutMapping("create")
    public ResponseEntity<String> createMunicipality(@RequestBody MunicipalityIF municipalityIF) {
        try {
            long id = municipalityHandler.createMunicipality(municipalityIF);
            return new ResponseEntity<>("Municipality created with ID: "+id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @JsonView(View.Detailed.class)
    @GetMapping("view/{id}")
    public ResponseEntity<MunicipalityOF> viewMunicipality(@PathVariable("id") final int id) {
        return new ResponseEntity<>(municipalityHandler.viewMunicipality(id), HttpStatus.OK);
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewAll")
    public ResponseEntity<List<MunicipalityOF>> viewAllMunicipalities() {
        return new ResponseEntity<>(municipalityHandler.viewAllMunicipalities(), HttpStatus.OK);
    }

    @GetMapping("criteria")
    public ResponseEntity<Set<String>> getSearchCriteria() {
        return new ResponseEntity<>(municipalityHandler.getSearchCriteria(), HttpStatus.OK);
    }

    @GetMapping("parameters")
    public ResponseEntity<Set<String>> getSearchParameters() {
        return new ResponseEntity<>(municipalityHandler.getSearchCriteria(), HttpStatus.OK);
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("search")
    public ResponseEntity<List<MunicipalityOF>> viewFilteredMunicipalities(@RequestBody List<SearchFilter> filters) {
        return new ResponseEntity<>(municipalityHandler.viewFilteredMunicipalities(filters), HttpStatus.OK);
    }

}

package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.UserHandler;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("user")
public class UserController {

    private final UserHandler userHandler;

    @Autowired
    public UserController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @PutMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody UserIF userIF) {
        try {
            long id = userHandler.registerUser(userIF);
            return new ResponseEntity<>("User created with ID: " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("generateAdmin/{municipalityID}")
    public ResponseEntity<String> generateMunicipalityAdministrator(@PathVariable long municipalityID,
                                                                    @RequestParam(name = "email") String email) {
        try {
            long id = userHandler.generateMunicipalityAdministrator(municipalityID, email);
            return new ResponseEntity<>("Administrator created with ID: " + id, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewMunicipalities")
    public ResponseEntity<List<MunicipalityOF>> viewMunicipalities() {
        return new ResponseEntity<>(userHandler.getMunicipalities(), HttpStatus.OK);
    }

    @PostMapping("isAllowedToModifyAuthorizations")
    public ResponseEntity<Object> isAllowedToModifyAuthorizations(@RequestParam long userID) {
        try {
            return new ResponseEntity<>(userHandler.isAllowedToModifyAuthorizations(userID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("modifyAuthorizations")
    public ResponseEntity<String> modifyAuthorizations(@RequestParam(name = "administratorID") long administratorID,
                                                       @RequestParam(name = "userID") long userID,
                                                       @RequestBody List<String> authorizations) {
        try {
            userHandler.modifyUserAuthorization(administratorID, userID, authorizations.stream().map(AuthorizationEnum::valueOf).toList());
            return new ResponseEntity<>("Authorizations modified", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("modifyUserAuthorizationsInMunicipality")
    public ResponseEntity<String> modifyUserAuthorizationInMunicipality(@RequestParam(name = "municipalityID") long municipalityID,
                                                                        @RequestParam(name = "administratorID") long administratorID,
                                                                        @RequestParam(name = "userID") long userID,
                                                                        @RequestBody List<AuthorizationEnum> authorizationsEnums) {
        try{
            userHandler.modifyUserAuthorization(municipalityID, administratorID, userID, authorizationsEnums);
            return new ResponseEntity<>("Authorizations modified", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @JsonView(View.Detailed.class)
    @GetMapping("view/{userID}")
    public ResponseEntity<Object> visualizeDetailedUser(@PathVariable long userID) {
        try {
            return new ResponseEntity<>(userHandler.visualizeDetailedUser(userID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewAdministratedMunicipalities/{userID}")
    public ResponseEntity<Object> getMunicipalitiesAdministratedByUser(@PathVariable long userID) {
        try {
            return new ResponseEntity<>(userHandler.getMunicipalitiesAdministratedByUser(userID), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @JsonView(View.Synthesized.class)
    @PostMapping("search")
    public ResponseEntity<Object> getFilteredUsers(@RequestBody List<SearchFilter> filters) {
        try {
            return new ResponseEntity<>(userHandler.getFilteredUsers(filters), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("criteria")
    public ResponseEntity<Set<String>> getUserSearchCriteria() {
        return new ResponseEntity<>(userHandler.getSearchCriteria(), HttpStatus.OK);
    }

    @GetMapping("parameters")
    public ResponseEntity<List<String>> getUserSearchParameters() {
        return new ResponseEntity<>(userHandler.getSearchParameters(), HttpStatus.OK);
    }

}

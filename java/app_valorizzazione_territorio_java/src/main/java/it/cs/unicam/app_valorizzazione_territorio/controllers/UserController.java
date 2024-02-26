package it.cs.unicam.app_valorizzazione_territorio.controllers;

import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.UserIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.MunicipalityOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.UserOF;
import it.cs.unicam.app_valorizzazione_territorio.handlers.UserHandler;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserHandler userHandler;

    @Autowired
    public UserController(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> registerUser(@RequestBody UserIF userIF) {
        return new ResponseEntity<>(userHandler.registerUser(userIF), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOF> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userHandler.visualizeDetailedUser(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/authorizations")
    public ResponseEntity<Void> updateUserAuthorizations(@PathVariable Long id,
                                                         @RequestBody List<AuthorizationEnum> newAuthorizations) {
        userHandler.modifyUserAuthorization(id, id, newAuthorizations); // Assuming the administrator ID is the same as the user ID
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/municipalities")
    public ResponseEntity<List<MunicipalityOF>> getMunicipalitiesAdministratedByUser(@PathVariable Long id) {
        return new ResponseEntity<>(userHandler.getMunicipalitiesAdministratedByUser(id), HttpStatus.OK);
    }
}
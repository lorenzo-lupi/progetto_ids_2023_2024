package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.RequestHandler;
import it.cs.unicam.app_valorizzazione_territorio.model.AuthorizationEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.ActivityTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.geolocatable.AttractionTypeEnum;
import it.cs.unicam.app_valorizzazione_territorio.model.requests.ModificationSetting;
import it.cs.unicam.app_valorizzazione_territorio.osm.Position;
import it.cs.unicam.app_valorizzazione_territorio.osm.utils.PositionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("request")
public class RequestController {

    @Value("${fileResources.path}")
    private String filePath;
    @Autowired
    RequestHandler requestHandler;

    @JsonView(View.Synthesized.class)
    @GetMapping("view/municipality/user/{userId}")
    public ResponseEntity<Object> viewMunicipalityRequests(@PathVariable("userId") long userId) {
        try {
            return new ResponseEntity<>(requestHandler.viewMunicipalityRequests(userId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnsupportedOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("view/contest/{userId}")
    public ResponseEntity<Object> viewContestRequests(@PathVariable("userId") long userId) {
        try {
            return new ResponseEntity<>(requestHandler.viewContestRequests(userId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnsupportedOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/municipality/{requestId}")
    public ResponseEntity<Object> viewMunicipalityRequest(@PathVariable("requestId") long requestId) {
        try {
            return new ResponseEntity<>(requestHandler.viewMunicipalityRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/contest/requests/{requestId}")
    public ResponseEntity<Object> viewContestRequest(@PathVariable("requestId") long requestId) {
        try {
            return new ResponseEntity<>(requestHandler.viewContestRequest(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("confirmationType/{requestId}")
    public ResponseEntity<Object> getConfirmationType(@PathVariable("requestId") long requestId) {
        try {
            return new ResponseEntity<>(requestHandler.getConfirmationType(requestId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("evaluate")
    public ResponseEntity<String> setApprovation(@RequestParam long requestId,
                                                 @RequestParam boolean isApproved) {
        try {
            requestHandler.setApprovation(requestId, isApproved);
            return new ResponseEntity<>("Request evaluated", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("delete/geoLocatable")
    public ResponseEntity<Object> deleteGeoLocatable(@RequestParam long userId,
                                                     @RequestParam long geoLocatableId,
                                                     @RequestParam String message) {
        try {
            long requestId = requestHandler.deleteGeoLocatable(userId, geoLocatableId, message);
            return new ResponseEntity<>("Deletion request dispatched with ID: "+requestId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("promotions")
    public ResponseEntity<Object> getPossiblePromotions() {
        return new ResponseEntity<>(requestHandler.obtainPossiblePromotions(), HttpStatus.OK);
    }

    @PostMapping("promote")
    public ResponseEntity<String> insertPromotionRequest(@RequestParam long userId,
                                                         @RequestParam long municipalityId,
                                                         @RequestParam String authorization,
                                                         @RequestParam String message) {
        try {
            long requestId = requestHandler.insertPromotionRequest(
                    userId, municipalityId, AuthorizationEnum.valueOf(authorization), message);
            return new ResponseEntity<>("Promotion request dispatched with ID: "+requestId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnsupportedOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("report/geoLocatable")
    public ResponseEntity<String> reportGeoLocatable(@RequestParam long geoLocatableId,
                                                     @RequestParam String message) {
        try {
            long requestId = requestHandler.reportGeoLocatable(geoLocatableId, message);
            return new ResponseEntity<>("Report request dispatched with ID: "+requestId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("report/content")
    public ResponseEntity<String> reportContent(@RequestParam long contentId,
                                               @RequestParam String message) {
        try {
            long requestId = requestHandler.reportContent(contentId, message);
            return new ResponseEntity<>("Report request dispatched with ID: "+requestId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("setters/geoLocatable")
    public ResponseEntity<Object> getGeoLocatableSetters(@RequestParam long geoLocatableId) {
        try {
            return new ResponseEntity<>(requestHandler.getGeoLocatableSetters(geoLocatableId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("modify/geoLocatable")
    public ResponseEntity<String> modifyGeoLocatable(@RequestParam long userId,
                                                     @RequestParam long geoLocatableId,
                                                     @RequestParam String message,
                                                     @RequestBody List<ModificationSetting> settings) {
        try {
            System.out.println(Proxy.isProxyClass(settings.get(0).value().getClass()));
            System.out.println(settings.get(0).value().getClass());
            long requestId = requestHandler.modifyGeoLocatable(userId, geoLocatableId,
                    settings.stream().map(s -> parse(s)).toList(), message);
            return new ResponseEntity<>("Modification request dispatched with ID: "+requestId, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ModificationSetting parse(ModificationSetting setting) {
        switch (setting.parameter()) {
            case "POSITION":
                if (setting.value() instanceof String s)
                    return new ModificationSetting(setting.parameter(), PositionParser.parse(s));
            case "ADD_FILE", "REMOVE_FILE":
                if (setting.value() instanceof String s)
                    return new ModificationSetting(setting.parameter(), new File(filePath+s));
            case "START_DATE", "END_DATE":
                if (setting.value() instanceof String s)
                    return new ModificationSetting(setting.parameter(), Date.parse(s));
            case "ACTIVITY_TYPE":
                if (setting.value() instanceof String s)
                    return new ModificationSetting(setting.parameter(), ActivityTypeEnum.valueOf(s));
            case "ATTRACTION_TYPE":
                if (setting.value() instanceof String s)
                    return new ModificationSetting(setting.parameter(), AttractionTypeEnum.valueOf(s));
            default:
                return setting;
        }
    }
}

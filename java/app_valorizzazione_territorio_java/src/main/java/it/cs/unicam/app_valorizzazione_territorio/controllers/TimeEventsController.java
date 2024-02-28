package it.cs.unicam.app_valorizzazione_territorio.controllers;

import it.cs.unicam.app_valorizzazione_territorio.handlers.TimeEventsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("timeEvents")
public class TimeEventsController {

    @Autowired
    private TimeEventsHandler timeEventsHandler;

    @PostMapping("endContest")
    public ResponseEntity<Object> endContest(@RequestParam long contestId) {
        try {
            timeEventsHandler.endContest(contestId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("startEvent")
    public ResponseEntity<Object> startEvent(@RequestParam long eventId) {
        try {
            timeEventsHandler.startEvent(eventId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.ContentHandler;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("content")
public class ContentController {
    @Autowired
    private ContentHandler contentHandler;

    @JsonView(View.Synthesized.class)
    @GetMapping("viewInPoi/{poiId}")
    public ResponseEntity<Object> viewApprovedContents(@PathVariable("poiId") long poiId) {
        try {
            return new ResponseEntity<>(contentHandler.viewApprovedContents(poiId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewInPoi/{poiId}/all")
    public ResponseEntity<Object> viewAllContents(@PathVariable("poiId") long poiId) {
        try {
            return new ResponseEntity<>(contentHandler.viewAllContents(poiId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("criteria")
    public ResponseEntity<Set<String>> getContentSearchCriteria() {
        return new ResponseEntity<>(contentHandler.getSearchCriteria(), HttpStatus.OK);
    }

    @GetMapping("parameters")
    public ResponseEntity<Set<String>> getContentSearchParameters() {
        return new ResponseEntity<>(contentHandler.getSearchParameters(), HttpStatus.OK);
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("searchInPoi/{poiId}")
    public ResponseEntity<Object> viewFilteredContents(@PathVariable("poiId") long poiId,
                                                       @RequestBody List<SearchFilter> filters) {
        try {
            return new ResponseEntity<>(contentHandler.viewFilteredContents(poiId, filters), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/{id}")
    public ResponseEntity<Object> viewContent(@PathVariable("id") final long id) {
        try {
            return new ResponseEntity<>(contentHandler.viewContent(id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("insert")
    public ResponseEntity<String> insertContent(@RequestParam long userId,
                                                @RequestParam long poiId,
                                                @RequestBody ContentIF content) {
        try {
            long id = contentHandler.insertContent(userId, poiId, content);
            return new ResponseEntity<>("Content created with ID: " + id, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("save")
    public ResponseEntity<String> saveContent(@RequestParam long userId,
                                              @RequestParam long contentId) {
        try {
            boolean isSaved = contentHandler.saveContent(userId, contentId);
            return new ResponseEntity<>((isSaved ? "Content saved" : "Content already saved"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("unsave")
    public ResponseEntity<String> unsaveContent(@RequestParam long userId,
                                                @RequestParam long contentId) {
        try {
            boolean isUnsaved = contentHandler.removeSavedContent(userId, contentId);
            return new ResponseEntity<>((isUnsaved ? "Content unsaved" : "Content already not saved"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewSaved/{userId}")
    public ResponseEntity<Object> viewSavedContents(@PathVariable("userId") long userId) {
        try {
            return new ResponseEntity<>(contentHandler.viewSavedContents(userId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

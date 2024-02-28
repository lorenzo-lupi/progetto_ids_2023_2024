package it.cs.unicam.app_valorizzazione_territorio.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContentIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.IF.ContestIF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.ContestOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.GeoLocatableOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.UserOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.OF.VotedContentOF;
import it.cs.unicam.app_valorizzazione_territorio.dtos.View;
import it.cs.unicam.app_valorizzazione_territorio.handlers.ContestHandler;
import it.cs.unicam.app_valorizzazione_territorio.search.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("contest")
public class ContestController {
    private final ContestHandler contestHandler;
    @Autowired
    public ContestController(ContestHandler contestHandler) {
        this.contestHandler = contestHandler;
    }

    @PostMapping("vote")
    public ResponseEntity<String> vote(@RequestParam long userID,
                                       @RequestParam long contestID,
                                       @RequestParam long contentID) {
        try{
            contestHandler.vote(userID, contestID, contentID);
            return ResponseEntity.ok("Vote added successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Vote not added", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @PostMapping("removeVote")
    public ResponseEntity<String> removeVote(@RequestParam long userID,
                                             @RequestParam long contestID) {
        try {
            contestHandler.removeVote(userID, contestID);
            return ResponseEntity.ok("Vote removed successfully");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("insertContent")
    public ResponseEntity<String> insertContent(@RequestParam long userID,
                                                @RequestParam long contestID,
                                                @RequestBody ContentIF contentIF) {
        try{
            long id = contestHandler.insertContent(userID, contestID, contentIF);
            return ResponseEntity.ok("Content created with ID: "+id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("viewProposals/all/{contestID}")
    public ResponseEntity<Object> viewAllProposals(@PathVariable long contestID) {
        try{
            List<VotedContentOF> votedContentOFs = contestHandler.viewAllProposals(contestID);
            return ResponseEntity.ok(votedContentOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewProposals/filtered/{contestID}")
    public ResponseEntity<Object> viewFilteredProposals(@PathVariable long contestID,
                                                        @RequestBody List<SearchFilter> filters) {
        try{
            List<VotedContentOF> votedContentOFs = contestHandler.viewFilteredProposals(contestID, filters);
            return ResponseEntity.ok(votedContentOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("viewProposal/{contestID}/{contentID}")
    public ResponseEntity<Object> viewProposal(@PathVariable long contestID,
                                               @PathVariable long contentID) {
        try{
            VotedContentOF votedContentOF = contestHandler.viewProposal(contestID, contentID);
            return ResponseEntity.ok(votedContentOF);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("insert")
    public ResponseEntity<String> insertContest(@RequestParam long userID,
                                                @RequestParam long municipalityID,
                                                @RequestBody ContestIF contestIF) {
        try{
            long id = contestHandler.insertContest(userID, municipalityID, contestIF);
            return ResponseEntity.ok("Contest created with ID: "+id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewUsers/filtered")
    public ResponseEntity<Object> viewFilteredUsers(@RequestBody List<SearchFilter> filters) {
        try{
            List<UserOF> userOFs = contestHandler.viewFilteredUsers(filters);
            return ResponseEntity.ok(userOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewGeoLocatables/filtered")
    public ResponseEntity<Object> viewFilteredGeoLocatables(@RequestBody List<SearchFilter> filters) {
        try{
            List<GeoLocatableOF> geoLocatableOFs = contestHandler.viewFilteredGeoLocatables(filters);
            return ResponseEntity.ok(geoLocatableOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("view/{contestID}")
    public ResponseEntity<Object> viewContest(@PathVariable long contestID) {
        try{
            ContestOF contestOF = contestHandler.viewContest(contestID);
            return ResponseEntity.ok(contestOF);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("view/all/{municipalityID}/{userID}")
    public ResponseEntity<Object> viewAllContests(@PathVariable long municipalityID,
                                                  @PathVariable long userID) {
        try{
            List<ContestOF> contestOFs = contestHandler.viewAllContests(userID, municipalityID);
            return ResponseEntity.ok(contestOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("view/filtered/{municipalityID}/{userID}")
    public ResponseEntity<Object> viewFilteredContests(@PathVariable long municipalityID,
                                                       @PathVariable long userID,
                                                       @RequestBody List<SearchFilter> filters) {
        try{
            List<ContestOF> contestOFs = contestHandler.viewFilteredContests(userID, municipalityID, filters);
            return ResponseEntity.ok(contestOFs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("criteria")
    public ResponseEntity<Object> getSearchCriteria() {
        try{
            Set<String> searchCriteria = contestHandler.getSearchCriteria();
            return ResponseEntity.ok(searchCriteria);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("parameters")
    public ResponseEntity<Object> getParameters() {
        try{
            List<String> parameters = contestHandler.getParameters();
            return ResponseEntity.ok(parameters);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

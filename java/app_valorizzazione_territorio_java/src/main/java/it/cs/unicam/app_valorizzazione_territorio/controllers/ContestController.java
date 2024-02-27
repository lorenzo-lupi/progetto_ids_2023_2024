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



    @PostMapping("vote/{userID}/{contestID}/{contentID}")
    public ResponseEntity<String> vote(@PathVariable long userID,
                                       @PathVariable long contestID,
                                       @PathVariable long contentID) {
        try{
            contestHandler.vote(userID, contestID, contentID);
            return ResponseEntity.ok("Vote added successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Vote not added", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @PostMapping("removeVote/{userID}/{contestID}")
    public ResponseEntity<String> removeVote(@PathVariable long userID,
                                             @PathVariable long contestID) {
        try{
            contestHandler.removeVote(userID, contestID);
            return ResponseEntity.ok("Vote removed successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("insertContent/{userID}/{contestID}")
    public ResponseEntity<Long> insertContent(@PathVariable long userID,
                                              @PathVariable long contestID,
                                              @RequestBody ContentIF contentIF) {
        try{
            long id = contestHandler.insertContent(userID, contestID, contentIF);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("viewAllProposals/{contestID}")
    public ResponseEntity<List<VotedContentOF>> viewAllProposals(@PathVariable long contestID) {
        try{
            List<VotedContentOF> votedContentOFs = contestHandler.viewAllProposals(contestID);
            return ResponseEntity.ok(votedContentOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewFilteredProposals/{contestID}")
    public ResponseEntity<List<VotedContentOF>> viewFilteredProposals(@PathVariable long contestID,
                                                                      @RequestBody List<SearchFilter> filters) {
        try{
            List<VotedContentOF> votedContentOFs = contestHandler.viewFilteredProposals(contestID, filters);
            return ResponseEntity.ok(votedContentOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("viewProposal/{contestID}/{contentID}")
    public ResponseEntity<VotedContentOF> viewProposal(@PathVariable long contestID,
                                                       @PathVariable long contentID) {
        try{
            VotedContentOF votedContentOF = contestHandler.viewProposal(contestID, contentID);
            return ResponseEntity.ok(votedContentOF);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("insertContest/{userID}/{municipalityID}")
    public ResponseEntity<Long> insertContest(@PathVariable long userID,
                                              @PathVariable long municipalityID,
                                              @RequestBody ContestIF contestIF) {
        try{
            long id = contestHandler.insertContest(userID, municipalityID, contestIF);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewFilteredUsers")
    public ResponseEntity<List<UserOF>> viewFilteredUsers(@RequestBody List<SearchFilter> filters) {
        try{
            List<UserOF> userOFs = contestHandler.viewFilteredUsers(filters);
            return ResponseEntity.ok(userOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewFilteredGeoLocatables")
    public ResponseEntity<List<GeoLocatableOF>> viewFilteredGeoLocatables(@RequestBody List<SearchFilter> filters) {
        try{
            List<GeoLocatableOF> geoLocatableOFs = contestHandler.viewFilteredGeoLocatables(filters);
            return ResponseEntity.ok(geoLocatableOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @GetMapping("viewAllContests/{userID}/{municipalityID}")
    public ResponseEntity<List<ContestOF>> viewAllContests(@PathVariable long userID,
                                                           @PathVariable long municipalityID) {
        try{
            List<ContestOF> contestOFs = contestHandler.viewAllContests(userID, municipalityID);
            return ResponseEntity.ok(contestOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Synthesized.class)
    @PostMapping("viewFilteredContests/{userID}/{municipalityID}")
    public ResponseEntity<List<ContestOF>> viewFilteredContests(@PathVariable long userID,
                                                                @PathVariable long municipalityID,
                                                                @RequestBody List<SearchFilter> filters) {
        try{
            List<ContestOF> contestOFs = contestHandler.viewFilteredContests(userID, municipalityID, filters);
            return ResponseEntity.ok(contestOFs);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getSearchCriteria")
    public ResponseEntity<Set<String>> getSearchCriteria() {
        try{
            Set<String> searchCriteria = contestHandler.getSearchCriteria();
            return ResponseEntity.ok(searchCriteria);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("getParameters")
    public ResponseEntity<List<String>> getParameters() {
        try{
            List<String> parameters = contestHandler.getParameters();
            return ResponseEntity.ok(parameters);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @JsonView(View.Detailed.class)
    @GetMapping("viewContest/{contestID}")
    public ResponseEntity<ContestOF> viewContest(@PathVariable long contestID) {
        try{
            ContestOF contestOF = contestHandler.viewContest(contestID);
            return ResponseEntity.ok(contestOF);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

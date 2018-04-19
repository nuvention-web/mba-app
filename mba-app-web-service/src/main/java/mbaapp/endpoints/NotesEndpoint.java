package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.requests.NotesRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 2/28/18.
 */

@RestController
@RequestMapping("/mba/users/{id}/school/{schoolShortName}/notes")
public class NotesEndpoint extends EndpointBase {

    Logger logger = Logger.getLogger(NotesEndpoint.class.getName());

    @PostMapping()
    @CrossOrigin
    @ApiOperation(value = "Add a new draft of an essay")
    public ResponseEntity<String> addNote(@RequestBody NotesRequest notesRequest, @PathVariable String id,
                                          @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.addNote(user, school, notesRequest);

            return new ResponseEntity<>("Added note", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


    @PutMapping("/{noteID}")
    @CrossOrigin
    @ApiOperation(value = "Add a new draft of an essay")
    public ResponseEntity<String> updateNote(@RequestBody NotesRequest notesRequest,@PathVariable String noteID,
                                             @PathVariable String id, @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.updateNote(user, school, notesRequest, noteID);

            return new ResponseEntity<>("Added note", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @DeleteMapping("/{noteID}")
    @CrossOrigin
    @ApiOperation(value = "Add a new draft of an essay")
    public ResponseEntity<String> deleteNote(@PathVariable String noteID,
                                             @PathVariable String id, @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.deleteNote(user, school, noteID);

            return new ResponseEntity<>("Added note", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


    @GetMapping(value = "/{noteID}", produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Add a new draft of an essay")
    public ResponseEntity<String> getNote(@PathVariable String noteID,
                                             @PathVariable String id, @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);
            return new ResponseEntity<>(userDBProvider.getNote(user, school, noteID).toString(), HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


}

package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.requests.NotesRequest;
import mbaapp.requests.RecommenderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 3/1/18.
 */

@RestController
@RequestMapping("/mba/users/{id}/school/{schoolShortName}/recommender")
public class RecommenderEndpoint extends EndpointBase {

    Logger logger = Logger.getLogger(NotesEndpoint.class.getName());


    @GetMapping("/{recommenderID}")
    @CrossOrigin
    @ApiOperation(value = "Update recommender notes")
    public ResponseEntity<String> getRecommendation(@PathVariable String recommenderID,
                                                         @PathVariable String id,
                                                         @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);
            return new ResponseEntity<String>(userDBProvider.getRecommender(user, school, recommenderID).toString(),HttpStatus.OK) ;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/{recommenderID}")
    @CrossOrigin
    @ApiOperation(value = "Update recommender notes")
    public ResponseEntity<String> updateRecommenderNotes(@RequestBody RecommenderRequest recommenderRequest,
                                                         @PathVariable String recommenderID,
                                                         @PathVariable String id,
                                                         @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool school = schoolExistsForUser(user, schoolShortName);
            userDBProvider.updateRecommender(user, school, recommenderRequest, recommenderID);
            return new ResponseEntity<>("Update recommender", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


}

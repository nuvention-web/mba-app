package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.EssayStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Created by jnag on 2/20/18.
 */
@RestController
@RequestMapping("/mba/users/{userEmail}/school/{schoolShortName}")
public class UsersSchoolEndpoint {

    Logger logger = Logger.getLogger(UsersEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    @GetMapping(produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Get school specific information")
    public ResponseEntity getUserSchool(@PathVariable String userEmail,
                                        @PathVariable String schoolShortName){

        try {
            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool userSchool = schoolExistsForUser(user, schoolShortName);

            return new ResponseEntity<>(userDBProvider.getUserSchoolDetail(user, userSchool).toString(), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }


    @PutMapping("/essay/{essayID}")
    @CrossOrigin
    @ApiOperation(value = "Update essay status")
    public ResponseEntity<String> addDraft(@RequestBody EssayStatusRequest essayStatusRequest, @PathVariable String userEmail,
                                           @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.updateEssayStatus(user, school, essayID, essayStatusRequest);

            return new ResponseEntity<>("Added draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/essay/{essayID}/draft")
    @CrossOrigin
    @ApiOperation(value = "Add a new essay for a particular school")
    public ResponseEntity<String> addDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                           @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.addEssayDraft(user, school, essayDraftRequest, essayID);

            return new ResponseEntity<>("Added draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @PutMapping("/essay/{essayID}/draft")
    @CrossOrigin
    @ApiOperation(value = "Add a new essay for a particular school")
    public ResponseEntity<String> updateDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                              @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.updateEssayDraft(user, school, essayDraftRequest, essayID);

            return new ResponseEntity<>("Added draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @DeleteMapping("/essay/{essayID}/draft")
    @CrossOrigin
    @ApiOperation(value = "Add a new essay for a particular school")
    public ResponseEntity<String> deleteDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                              @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.deleteEssayDraft(user, school, essayDraftRequest, essayID);

            return new ResponseEntity<>("Added draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }





    private ResponseEntity<String> runValidations(String userEmail, String schoolShortName) {
        User user = userDBProvider.getUser(userEmail);
        if (user == null) {
            return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
        }

        UserSchool school = schoolExistsForUser(user, schoolShortName);

        if (school == null) {
            return new ResponseEntity<String>("The school " + schoolShortName + " is not a part of the list of schools " +
                    "for " + userEmail, HttpStatus.NOT_ACCEPTABLE);
        }

        return null;

    }


    private UserSchool schoolExistsForUser(User user, String schoolShortName) {
        for (UserSchool school : user.getSchools()) {
            if (school.getShortName().equalsIgnoreCase(schoolShortName)) {
                return school;
            }
        }

        return null;
    }


}

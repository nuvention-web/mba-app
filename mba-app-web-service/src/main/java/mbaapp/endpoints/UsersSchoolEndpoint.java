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
@RequestMapping("/mba/users/{id}/school/{schoolShortName}")
public class UsersSchoolEndpoint extends EndpointBase {

    Logger logger = Logger.getLogger(UsersEndpoint.class.getName());

    @GetMapping(produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Get school specific information")
    public ResponseEntity getUserSchool(@PathVariable String id,
                                        @PathVariable String schoolShortName) {

        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool userSchool = schoolExistsForUser(user, schoolShortName);

            return new ResponseEntity<>(userDBProvider.getUserSchoolDetail(user, userSchool).toString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deadline")
    @CrossOrigin
    @ApiOperation(value = "Set deadline")
    public ResponseEntity setDeadline(@PathVariable String id,
                                      @PathVariable String schoolShortName,
                                      @RequestBody String deadline) {


        try {
            if (runValidations(id, schoolShortName) != null) {
                return runValidations(id, schoolShortName);
            }

            User user = userDBProvider.getUser(id);
            UserSchool userSchool = schoolExistsForUser(user, schoolShortName);
            userDBProvider.setDeadlineForSchool(user, userSchool, deadline);

            return new ResponseEntity<>("Added deadline", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


}

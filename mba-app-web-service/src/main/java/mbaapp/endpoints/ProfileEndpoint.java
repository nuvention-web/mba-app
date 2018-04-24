package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.User;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.ProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Created by jnag on 2/26/18.
 */
@RestController
@RequestMapping("/mba/users/{userEmail}/profile")
public class ProfileEndpoint extends EndpointBase{


    Logger logger = Logger.getLogger(ProfileEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    @PutMapping()
    @CrossOrigin
    @ApiOperation(value = "Update the user's profile")
    public ResponseEntity<String> updateProfile(@PathVariable String userEmail, @RequestBody ProfileRequest profileRequest) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.updateUserProfile(user, profileRequest);

            return new ResponseEntity<>("Updated user profile", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Returns the user's profile")
    public ResponseEntity<String> getUserProfile(@PathVariable String userEmail) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userDBProvider.getUserProfile(user).toString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

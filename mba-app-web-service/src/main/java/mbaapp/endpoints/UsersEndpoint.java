package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.requests.*;
import mbaapp.core.User;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.providers.UserDBProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * Created by jnag on 2/15/18.
 */
@RestController
@RequestMapping("/mba/users")
public class UsersEndpoint extends EndpointBase{

    Logger logger = Logger.getLogger(UsersEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;

    @Autowired
    public JavaMailSender emailSender;


    @GetMapping(value = "/{userEmail:.+}", produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Retrieve a user - this will be used for the home page")
    public ResponseEntity getUser(@PathVariable String userEmail) {
        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User mbaUser = userDBProvider.getUser(userEmail);

            if (mbaUser == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            }

            mbaUser.setLastLogin(getDate());
            userDBProvider.saveUser(mbaUser);

            return new ResponseEntity<>(userDBProvider.getUserSchoolDetails(mbaUser).toString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{userEmail}/school")
    @CrossOrigin
    @ApiOperation(value = "Add schools to the list of schools the user is interested in ")
    public ResponseEntity<String> addSchool(@RequestBody AddSchoolsRequest userRequest, @PathVariable String userEmail) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }
            userDBProvider.addSchools(userRequest, user);
            return new ResponseEntity<>("Updated user", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/{userEmail}/recommender")
    @CrossOrigin
    @ApiOperation(value = "Add recommenders for a user ")
    public ResponseEntity<String> addRecommender(@RequestBody AddRecommendersRequest recommendersRequest, @PathVariable String userEmail) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }
            userDBProvider.addRecommenders(recommendersRequest, user);
            return new ResponseEntity<>("Updated user", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{userEmail}/recommender/{recommender}")
    @ApiOperation(value = "Remove a particular recommender from the user's recommenders.")
    public ResponseEntity<String> deleteRecommender(@PathVariable String userEmail, @PathVariable String recommender) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.deleteRecommender(user, recommender);
            return new ResponseEntity<>("Deleted recommender", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/{userEmail}/school/{schoolName}")
    @CrossOrigin
    @ApiOperation(value = "Remove a particular school from the user's list of schools ")
    public ResponseEntity<String> deleteSchool(@PathVariable String userEmail, @PathVariable String schoolName) {

        try {

            if(runValidations(userEmail, schoolName)!=null){
                return runValidations(userEmail, schoolName);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.deleteSchool(user, schoolName);

            return new ResponseEntity<>("Did not delete school - for demo!", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping(value = "/{userEmail}/essays", produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Retrieve a user - this will be used for the home page")
    public ResponseEntity getUserEssays(@PathVariable String userEmail) {
        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userDBProvider.getAllUserEssays(user).toString(), HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/{userEmail}/scores")
    @CrossOrigin
    @ApiOperation(value = "Add scores for a user ")
    public ResponseEntity<String> addScore(@RequestBody ScoreRequest scoreRequest, @PathVariable String userEmail) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.addScores(user, scoreRequest);

            return new ResponseEntity<>("Added scores.", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value =  "/{userEmail}/scores", produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Get scores for a user. ")
    public ResponseEntity<String> getScores(@PathVariable String userEmail) {

        try {

            if(runValidations(userEmail, null)!=null){
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            JSONObject scoresJSON = userDBProvider.getScores(user);

            return new ResponseEntity<>(scoresJSON.toString(), HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/{userEmail}/changePassword")
    @CrossOrigin
    @ApiOperation(value = "Change password")
    public ResponseEntity<String> forgotPassword(@RequestBody ChangePasswordRequest request,
                                                 @PathVariable String userEmail) {

        try {
            User user = userDBProvider.getUserByEmail(userEmail);

            if (user == null) {
                return new ResponseEntity<>("Did not find an account with this email", HttpStatus.BAD_REQUEST);

            }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if(!bCryptPasswordEncoder.matches(java.nio.CharBuffer.wrap(request.getCurrentPassword()), user.getPassword())) {
                return new ResponseEntity<>("The password supplied does not match with the current password", HttpStatus.BAD_REQUEST);
            }

            userDBProvider.changePassword(user, request.getNewPassword().toCharArray(), false);

            return new ResponseEntity<>("Changed password", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

}

package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.requests.AddRecommendersRequest;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.AddSchoolsRequest;
import mbaapp.core.User;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.providers.UserDBProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
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
public class UsersEndpoint {

    Logger logger = Logger.getLogger(UsersEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;


    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation(value = "Create a new user")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest createUserRequest) {
        try {

            if (createUserRequest.getEmail().isEmpty()) {
                return new ResponseEntity<String>("Missing email", HttpStatus.BAD_REQUEST);
            }

            if (userDBProvider.getUser(createUserRequest.getEmail()) != null) {
                return new ResponseEntity<String>("User exists", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.addUser(createUserRequest);

            return new ResponseEntity<>("Added user", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userEmail:.+}", produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Retrieve a user")
    public ResponseEntity getUser(@PathVariable String userEmail) {
        try {
            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userDBProvider.getUserDetails(user).toString(), HttpStatus.OK);
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
            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.deleteSchool(user, schoolName);

            return new ResponseEntity<>("Deleted school", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}

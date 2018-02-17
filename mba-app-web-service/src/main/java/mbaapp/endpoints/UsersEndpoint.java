package mbaapp.endpoints;

import mbaapp.core.User;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.providers.UserDBProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.data.repository.init.ResourceReader.Type.JSON;

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
    public ResponseEntity<String> addUser(@RequestBody String payloadString) {
        try {

            JSONObject payload = new JSONObject(payloadString);
            String email = payload.optString("email");
            if (email.isEmpty()) {
                return new ResponseEntity<String>("Missing email", HttpStatus.BAD_REQUEST);
            }

            if (userDBProvider.getUser(email) != null) {
                return new ResponseEntity<String>("User exists", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.addUser(payload);

            return new ResponseEntity<>("Added user", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userEmail}", produces = "application/json")
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


    @PostMapping("/{userEmail}")
    public ResponseEntity<String> addUser(@RequestBody String payloadString, @PathVariable String userEmail) {

        try {
            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }
            JSONObject payload = new JSONObject(payloadString);
            userDBProvider.addSchool(payload, user);
            return new ResponseEntity<>("Updated user", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{userEmail}/school")
    public ResponseEntity<String> deleteSchool(@PathVariable String userEmail, @RequestBody String payloadString ) {

        try {
            User user = userDBProvider.getUser(userEmail);
            if (user == null) {
                return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
            }

            JSONObject payload = new JSONObject(payloadString);
            String schoolName = payload.getString("name");
            userDBProvider.deleteSchool(user, schoolName);

            return new ResponseEntity<>("Deleted school", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}

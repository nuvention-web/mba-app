package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.InactiveUser;
import mbaapp.email.EmailService;
import mbaapp.providers.InactiveUserDBProvider;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.ActivateUserRequest;
import mbaapp.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 4/19/18.
 */
@RestController
@RequestMapping("/account")
public class AccountEndpoint {

    Logger logger = Logger.getLogger(UsersEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    @Autowired
    @Qualifier("inactiveUserMongoDB")
    InactiveUserDBProvider inactiveDBProvider;

    @Autowired
    EmailService emailService;

    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;

    @Autowired
    public JavaMailSender emailSender;



    @PostMapping("/create")
    @CrossOrigin
    @ApiOperation(value = "Create a new user")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest createUserRequest) {
        try {

            if (createUserRequest.getEmail().isEmpty()) {
                return new ResponseEntity<String>("Missing email", HttpStatus.BAD_REQUEST);
            }

            if (createUserRequest.getPassword()==null || createUserRequest.getPassword().length==0) {
                return new ResponseEntity<String>("Missing password", HttpStatus.BAD_REQUEST);
            }

            if (userDBProvider.getUserByEmail(createUserRequest.getEmail()) != null ||
                    inactiveDBProvider.getInactiveUser(createUserRequest.getEmail())!=null) {
                return new ResponseEntity<String>("User exists", HttpStatus.NOT_ACCEPTABLE);
            }

            InactiveUser inactiveUser = inactiveDBProvider.createUser(createUserRequest);

            emailService.sentActivationEmail(emailSender, inactiveUser);

            inactiveDBProvider.saveUser(inactiveUser);

            return new ResponseEntity<>("Added user", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/activate")
    @CrossOrigin
    @ApiOperation(value = "Activates an user")
    public ResponseEntity<String> activateUser(@RequestBody ActivateUserRequest activateUserRequest) {
        try {

            if (activateUserRequest.getEmail().isEmpty()) {
                return new ResponseEntity<String>("Missing email", HttpStatus.BAD_REQUEST);
            }

            if(inactiveDBProvider.getInactiveUser(activateUserRequest.getEmail())==null){
                return new ResponseEntity<>("Did not find the user to activate", HttpStatus.NOT_FOUND);
            }

            InactiveUser inactiveUser = inactiveDBProvider.getInactiveUser(activateUserRequest.getEmail());
            if(inactiveUser.getCode().equals(activateUserRequest.getCode())) {
                userDBProvider.activateUser(inactiveUser);
                inactiveDBProvider.deleteInactiveUser(inactiveUser);
            }

            else {
                return new ResponseEntity<>("Invalid activation code.", HttpStatus.BAD_REQUEST);

            }

            return new ResponseEntity<>("Added user", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




//    @PostMapping("/login")
//    @CrossOrigin
//    @ApiOperation(value = "Create a new user")
//    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
//        try {
//
//            User user = userDBProvider.getUserByEmail(loginRequest.getEmail());
//
//            if (user == null) {
//                return new ResponseEntity<String>("Did not find the user", HttpStatus.NOT_FOUND);
//            }
//
//
//            if(userDBProvider.authenticateUser(user, loginRequest)){
//                return new ResponseEntity<>("Accepted", HttpStatus.OK);
//            }
//
//            else return new ResponseEntity<>("Incorrect credentials", HttpStatus.UNAUTHORIZED);
//
//
//
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, e.getMessage(), e);
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }


}

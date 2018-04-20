package mbaapp.endpoints;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

import io.swagger.annotations.ApiOperation;
import mbaapp.core.User;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

            if (createUserRequest.getPassword()==null || createUserRequest.getPassword().length==0) {
                return new ResponseEntity<String>("Missing password", HttpStatus.BAD_REQUEST);
            }

            if (userDBProvider.getUserByEmail(createUserRequest.getEmail()) != null) {
                return new ResponseEntity<String>("User exists", HttpStatus.NOT_ACCEPTABLE);
            }

            userDBProvider.addUser(createUserRequest);

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

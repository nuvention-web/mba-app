package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.User;
import mbaapp.providers.UserDBProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 6/4/18.
 */
@RestController
@RequestMapping("/mba/users")
public class RecommendationEndpoint extends EndpointBase{

    Logger logger = Logger.getLogger(RecommendationEndpoint.class.getName());

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;


    @PostMapping("/{userEmail}/recommendation")
    @CrossOrigin
    @ApiOperation(value = "Add a task ")
    public ResponseEntity generateRecommendation(@PathVariable String userEmail) {

        try {
            if (runValidations(userEmail, null) != null) {
                return runValidations(userEmail, null);
            }

            User mbaUser = userDBProvider.getUser(userEmail);

            if (mbaUser == null) {
                return new ResponseEntity<>("User does not exist!", HttpStatus.BAD_REQUEST);
            }


            userDBProvider.generateRecommendation(mbaUser);

            return new ResponseEntity<>("Generated recommendation", HttpStatus.OK);

        }

        catch (Exception e) {

            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);


        }



    }




}

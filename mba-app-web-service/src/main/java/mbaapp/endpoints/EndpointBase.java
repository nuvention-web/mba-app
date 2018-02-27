package mbaapp.endpoints;

import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by jnag on 2/27/18.
 */
public class EndpointBase {


    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    protected ResponseEntity<String> runValidations(String userEmail, String schoolShortName) {
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


    protected UserSchool schoolExistsForUser(User user, String schoolShortName) {
        for (UserSchool school : user.getSchools()) {
            if (school.getShortName().equalsIgnoreCase(schoolShortName)) {
                return school;
            }
        }

        return null;
    }
}

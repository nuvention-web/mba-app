package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 5/1/18.
 */
@RestController
@RequestMapping("/download")
public class DownloadDraftEndpoint extends EndpointBase{

    Logger logger = Logger.getLogger(EssaysEndpoint.class.getName());

    @GetMapping(value = "/users/{userEmail}/school/{schoolShortName}/essay/{essayID}/draft/{draftID}", produces = "application/octet-stream")
    @CrossOrigin
    @ApiOperation(value = "Download an essay draft")
    public ResponseEntity downloadDraft(@PathVariable String userEmail, @PathVariable String schoolShortName,
                                        @PathVariable String essayID, @PathVariable String draftID) {

        try {
            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);

            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+draft.getDraftName());

            ResponseEntity responseEntity =  new ResponseEntity(userDBProvider.getEssayDraftUploaded(user,school, essayID, draftID).toByteArray(), headers,
                    HttpStatus.OK) ;

            return responseEntity;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    protected ResponseEntity<String> runValidations(String userEmail, String schoolShortName) {

        User user = userDBProvider.getUser(userEmail);
        if (user == null) {
            return new ResponseEntity<String>("User does not exist!", HttpStatus.NOT_ACCEPTABLE);
        }

        if(schoolShortName==null) {
            return null;
        }

        UserSchool school = getSchoolForUser(user, schoolShortName);

        if (school == null) {
            return new ResponseEntity<String>("The school " + schoolShortName + " is not a part of the list of schools " +
                    "for " + userEmail, HttpStatus.NOT_ACCEPTABLE);
        }

        return null;

    }
}

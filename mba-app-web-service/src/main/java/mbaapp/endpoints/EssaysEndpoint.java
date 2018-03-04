package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.requests.EssayStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 2/27/18.
 */
@RestController
@RequestMapping("/mba/users/{userEmail}/school/{schoolShortName}/essay/{essayID}")
public class EssaysEndpoint extends EndpointBase{

    Logger logger = Logger.getLogger(EssaysEndpoint.class.getName());

    @PutMapping()
    @CrossOrigin
    @ApiOperation(value = "Update essay status")
    public ResponseEntity<String> updateEssayStatus(@RequestBody EssayStatusRequest essayStatusRequest, @PathVariable String userEmail,
                                           @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.updateEssayStatus(user, school, essayID, essayStatusRequest);

            return new ResponseEntity<>("Updated essay status", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping(produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "Get essay details of a particular essay")
    public ResponseEntity<String> getEssay(@PathVariable String userEmail,
                                           @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {
            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            return new ResponseEntity<>(userDBProvider.getEssay(user, school, essayID).toString(), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/draft")
    @CrossOrigin
    @ApiOperation(value = "Add a new draft of an essay")
    public ResponseEntity<String> addDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                           @PathVariable String schoolShortName, @PathVariable String essayID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.addEssayDraft(user, school, essayDraftRequest, essayID, keywords);

            return new ResponseEntity<>("Added draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @PutMapping("/draft/{draftID}")
    @CrossOrigin
    @ApiOperation(value = "Update an essay draft")
    public ResponseEntity<String> updateDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                              @PathVariable String schoolShortName, @PathVariable String essayID,
                                              @PathVariable String draftID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.updateEssayDraft(user, school, essayDraftRequest, essayID, draftID, keywords.schoolKeywords);

            return new ResponseEntity<>("Updated draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @DeleteMapping("/draft/{draftID}")
    @CrossOrigin
    @ApiOperation(value = "Delete an essay draft")
    public ResponseEntity<String> deleteDraft(@RequestBody EssayDraftRequest essayDraftRequest, @PathVariable String userEmail,
                                              @PathVariable String schoolShortName, @PathVariable String essayID,
                                              @PathVariable String draftID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.deleteEssayDraft(user, school, essayDraftRequest, essayID, draftID);

            return new ResponseEntity<>("Deleted draft", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping("/upload/draft")
    @CrossOrigin
    @ApiOperation(value = "Upload an essay draft")
    public ResponseEntity<String> uploadDraft(@RequestParam("file") MultipartFile file,
                                              @PathVariable String userEmail, @PathVariable String schoolShortName,
                                              @PathVariable String essayID) {

        try {
            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            userDBProvider.addEssayDraftUpload(user,school,file, essayID);

            return new ResponseEntity<>("Uploaded draft", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


    @GetMapping(value = "/download/draft/{draftID}", produces = "application/octet-stream")
    @CrossOrigin
    @ApiOperation(value = "Download an essay draft")
    public ResponseEntity downloadDraft(@PathVariable String userEmail, @PathVariable String schoolShortName,
                                                @PathVariable String essayID, @PathVariable String draftID) {

        try {
            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = schoolExistsForUser(user, schoolShortName);

            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+draft.getDraftName());

            ResponseEntity responseEntity =  new ResponseEntity(userDBProvider.downloadDraft(user,school, essayID, draftID).toByteArray(), headers,
                    HttpStatus.OK) ;

            return responseEntity;

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }



}

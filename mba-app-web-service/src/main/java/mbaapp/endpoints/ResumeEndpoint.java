package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.Resume;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.services.ResumeService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 5/12/18.
 */

@RestController
@RequestMapping("/mba/users/{userEmail}/resume")
public class ResumeEndpoint extends EndpointBase {


    Logger logger = Logger.getLogger(EssaysEndpoint.class.getName());

    @Autowired
    ResumeService resumeService;

    @GetMapping(produces = "application/json")
    @CrossOrigin
    public ResponseEntity<String> getResumes(@PathVariable String userEmail) {

        try {
            if (runValidations(userEmail, null) != null) {
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            List<Resume> resumes = user.getResumes();
            if (resumes == null) {
                resumes = new ArrayList<>();
            }

            JSONObject resumesJSON = new JSONObject();
            JSONArray resumesArray = new JSONArray();
            resumesJSON.put("resumes", resumesArray);
            for (Resume resume : resumes) {
                resumesArray.put(resume.toJSON());
            }

            return new ResponseEntity<>(resumesJSON.toString(), HttpStatus.OK);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


    @PostMapping("/upload")
    @CrossOrigin
    @ApiOperation(value = "Upload a resume")
    public ResponseEntity<String> uploadDraft(@RequestParam("file") MultipartFile file,
                                              @PathVariable String userEmail) {

        try {
            if (runValidations(userEmail, null) != null) {
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);

            userDBProvider.addResume(user, file);

            return new ResponseEntity<>("Uploaded resume", HttpStatus.CREATED);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }


    @PostMapping("/{resumeID}/analysis")
    @CrossOrigin
    @ApiOperation(value = "Resume analysis")
    public ResponseEntity<String> analysis(@PathVariable("resumeID") String resumeID,
                                            @PathVariable String userEmail) {

        try {
            if (runValidations(userEmail, null) != null) {
                return runValidations(userEmail, null);
            }

            User user = userDBProvider.getUser(userEmail);
            Resume resume = userDBProvider.getResume(user, resumeID);

            if(resume == null) {
                return new ResponseEntity<String>("Did not find resume", HttpStatus.BAD_REQUEST);
            }

            byte[] resumeContents = userDBProvider.getResumeUpload(user, resume).toByteArray();
            resumeService.runAnalysis(user, resumeContents, resume);
            userDBProvider.saveUser(user);

            return new ResponseEntity<>("Done analysis", HttpStatus.CREATED);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

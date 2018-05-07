package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.*;
import mbaapp.mongoDB.SchoolInfoRepository;
import mbaapp.requests.ReviewRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 4/14/18.
 */
@RestController
@RequestMapping("/review/users/{userEmail}/school/{schoolShortName}/essay/{essayID}/draft/{draftID}/review/{reviewID}")
public class ReviewsEndpoint extends EndpointBase {

    Logger logger = Logger.getLogger(ReviewsEndpoint.class.getName());

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;


    @GetMapping(produces = "application/json")
    @CrossOrigin
    @ApiOperation(value = "View a review request")
    public ResponseEntity<String> getReview(@PathVariable String userEmail, @PathVariable String schoolShortName,
                                            @PathVariable String essayID, @PathVariable String draftID, @PathVariable String reviewID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);

            Essay essay = school.getEssay(essayID);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(school.getShortName());

            String essayPrompt = "";

            for (SchoolInfoEssay schoolInfoEssay : schoolInfo.getEssays()) {
                if (schoolInfoEssay.getEssayID().equalsIgnoreCase(essayID)) {
                    essayPrompt = schoolInfoEssay.getEssayPrompt();
                }
            }
            JSONObject reviewEndpointJSON = new JSONObject();

            boolean reviewFound = false;
            for (Review review : draft.getReviews()) {
                if (review.getID().equalsIgnoreCase(reviewID)) {
                    reviewFound = true;
                    JSONObject reviewJSON = review.toJSON();
                    reviewEndpointJSON.put("school", schoolInfo.getName());
                    reviewEndpointJSON.put("reviewer", review.getName());
                    reviewEndpointJSON.put("user", user.getName());
                    reviewEndpointJSON.put("prompt", essayPrompt);
                    reviewEndpointJSON.put("review", review.toJSON());
                    reviewEndpointJSON.put("draft", draft.getContents());
                    reviewEndpointJSON.put("personalMessage", review.getPersonalMessage());
                }
            }

            return new ResponseEntity<>(reviewEndpointJSON.toString(), HttpStatus.OK);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping()
    @CrossOrigin
    @ApiOperation(value = "View a review request")
    public ResponseEntity<String> addComments(@RequestBody ReviewRequest reviewRequest, @PathVariable String userEmail, @PathVariable String schoolShortName,
                                              @PathVariable String essayID, @PathVariable String draftID, @PathVariable String reviewID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);

            Essay essay = school.getEssay(essayID);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            boolean foundReview = false;
            for(Review review : draft.getReviews()) {
                if(review.getID().equalsIgnoreCase(reviewID)) {
                    ReviewComments reviewComments = review.getReviewComments() == null ?
                            new ReviewComments() : review.getReviewComments();
                    reviewComments.setDate(getDate());
                    reviewComments.setComment(reviewRequest.getComments());
                    foundReview = true;
                    review.setReviewComments(reviewComments);
                }
            }

            userDBProvider.saveUser(user);

            if(!foundReview) {
                throw new Exception("Did not find a review with the review ID: "+reviewID);
            }

            return new ResponseEntity<>("Added review comment", HttpStatus.OK);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }

    }


    @PostMapping("/upload")
    @CrossOrigin
    @ApiOperation(value = "View a review request")
    public ResponseEntity<String> uploadReviewDraft(@PathVariable String userEmail, @PathVariable String schoolShortName,
                                                    @PathVariable String essayID, @PathVariable String draftID, @PathVariable String reviewID,
                                                    @RequestParam("file") MultipartFile file) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);

            Essay essay = school.getEssay(essayID);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);
            boolean foundReview = false;
            ReviewComments reviewComments = null;
            for (Review review : draft.getReviews()) {
                if (review.getID().equalsIgnoreCase(reviewID)) {
                    foundReview = true;
                    reviewComments = review.getReviewComments() == null ? new ReviewComments() : review.getReviewComments();
                    reviewComments.setDate(getDate());
                    reviewComments.setFileName(file.getOriginalFilename());
                    review.setReviewComments(reviewComments);
                }
            }

            if (!foundReview) {
                throw new Exception("Did not find a review with the review ID: " + reviewID);
            }

            if (file != null) {
                userDBProvider.addReviewDraft(user, school, file, reviewComments);
            }

            return new ResponseEntity<>("Added review comment", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }




    @GetMapping("/download")
    @CrossOrigin
    @ApiOperation(value = "View a review request")
    public ResponseEntity<String> downloadReviewDraft(@PathVariable String userEmail, @PathVariable String schoolShortName,
                                                      @PathVariable String essayID, @PathVariable String draftID, @PathVariable String reviewID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);

            Essay essay = school.getEssay(essayID);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            boolean foundReview = false;
            Review reviewRequested = null;
            for(Review review : draft.getReviews()) {
                if(review.getID().equalsIgnoreCase(reviewID)) {
                    if(review.getReviewComments().getUploadID()==null) {
                        throw new Exception("No file was uploaded by this reviewer");
                    }
                    foundReview = true;
                    reviewRequested = review;
                    break;
                }
            }

            if(!foundReview) {
                throw new Exception("Did not find a review with the review ID: "+reviewID);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
                    +reviewRequested.getReviewComments().getFileName());

            ResponseEntity responseEntity =  new ResponseEntity(userDBProvider.getFileUploaded
                    (reviewRequested.getReviewComments().getUploadID()).toByteArray(), headers, HttpStatus.OK) ;

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


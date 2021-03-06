package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.services.AnalysisService;
import mbaapp.services.PlagiarismService;
import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.requests.EssayStatusRequest;
import mbaapp.services.ProofReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jnag on 5/2/18.
 */
@RestController
@RequestMapping("/mba/users/{userEmail}/school/{schoolShortName}/essay/{essayID}/draft/{draftID}/scan")
public class ScanEndpoints extends EndpointBase {

    Logger logger = Logger.getLogger(ScanEndpoints.class.getName());

    @Autowired
    PlagiarismService plagiarismService;

    @Autowired
    AnalysisService analysisService;

    @Autowired
    ProofReadingService proofReadingService;


    @PostMapping("/analysis")
    @CrossOrigin
    @ApiOperation(value = "Update essay status")
    public ResponseEntity<String> textanalysis(@RequestBody EssayStatusRequest essayStatusRequest, @PathVariable String userEmail,
                                                    @PathVariable String schoolShortName, @PathVariable String essayID,
                                                    @PathVariable String draftID) {
        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);

            analysisService.runAnalysis(user, school, essayID, draftID, draft);
            userDBProvider.saveUser(user);

            return new ResponseEntity<>("Scanned draft", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }



    @PostMapping("/proofRead")
    @CrossOrigin
    @ApiOperation(value = "Proofread API")
    public ResponseEntity<String> proofRead(@RequestBody EssayStatusRequest essayStatusRequest, @PathVariable String userEmail,
                                             @PathVariable String schoolShortName, @PathVariable String essayID,
                                             @PathVariable String draftID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);
            proofReadingService.proofRead(user, school, essayID, draftID, draft);
            userDBProvider.saveUser(user);

            return new ResponseEntity<>("Scanned draft", HttpStatus.OK);


        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("/plagiarism")
    @CrossOrigin
    @ApiOperation(value = "Plagiarism API")
    public ResponseEntity<String> plagiarism(@RequestBody EssayStatusRequest essayStatusRequest, @PathVariable String userEmail,
                                                    @PathVariable String schoolShortName, @PathVariable String essayID,
                                                    @PathVariable String draftID) {

        try {

            if (runValidations(userEmail, schoolShortName) != null) {
                return runValidations(userEmail, schoolShortName);
            }

            User user = userDBProvider.getUser(userEmail);
            UserSchool school = getSchoolForUser(user, schoolShortName);
            EssayDraft draft = school.getEssayDraft(essayID, draftID);
            plagiarismService.runPlagiarismCheck(user, school, essayID, draftID, draft);
//            JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
//            if (draft.getDraftType().equals(EssayDraft.DraftType.UPLOAD)) {
//                String token = plagiarismService.getToken();
//                Pattern pattern = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);
//                Matcher matcher = pattern.matcher(draft.getContents());
//                String paragraph = "";
//                while (matcher.find()) {
//                    paragraph = matcher.group(1).trim();
////                    System.out.println(paragraph);
//                    StringBuilder stringBuilder = new StringBuilder(paragraph);
//                    List<RuleMatch> matches = langTool.check(paragraph);
//                    int totalShift=0;
//                    for (RuleMatch match : matches) {
//                        int fromPos = match.getFromPos();
//                        int toPos = match.getToPos();
//                        System.out.println(fromPos+" "+toPos);
//                        String spanStart = MessageFormat.format("<span data-toggle=\"m-popover\" data-placement=\"left\" title=\"{0} Suggestions: {1}\"><u>",
//                                match.getMessage(), match.getSuggestedReplacements());
//                        String spanEnd = "</u></span>";
//                        stringBuilder.insert(fromPos + totalShift, spanStart);
//                        stringBuilder.insert(spanStart.length()+toPos + totalShift, spanEnd);
//                        totalShift = spanEnd.length() + spanStart.length() + totalShift;
//                    }
//                    System.out.println(stringBuilder.toString());
//                }
//            }

            return new ResponseEntity<>("Scanned draft", HttpStatus.OK);

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

}



package mbaapp.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import mbaapp.core.*;
import mbaapp.email.EmailService;
import mbaapp.requests.*;
import mbaapp.providers.UserDBProvider;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jnag on 2/15/18.
 */
@Service("userMongoDB")
public class MongoUserDBProvider implements UserDBProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    Keywords keywords;

    @Autowired
    EmailService emailService;

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public User getUser(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public JSONObject getAllUserEssays(User user) throws Exception{

        JSONObject userEssays = new JSONObject();

        for(UserSchool userSchool : user.getSchools()) {
            JSONObject schoolEssayJSON = new JSONObject();
            userEssays.put(userSchool.getShortName(), schoolEssayJSON);
            for(Essay essay : userSchool.getEssays()) {
                int totalDrafts = essay.getDrafts().size();
                if(totalDrafts>0) {
                    EssayDraft lastDraft = essay.getDrafts().get(totalDrafts - 1);
                    JSONObject draftJSON = lastDraft.toJSON();
                    draftJSON.put("essayID", essay.getEssayID());
                    draftJSON.put("prompt", getEssayPrompt(essay.getEssayID(), userSchool.getShortName()));
                    schoolEssayJSON.put(essay.getEssayID(), draftJSON);
                }

            }
        }

        return userEssays;
    }

    public void saveUser(User user) throws Exception
    {
        userRepository.save(user);
    }

    private String getEssayPrompt(String essayID, String schoolName) {
        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(schoolName);
        for(SchoolInfoEssay essay : schoolInfo.getEssays()) {
            if(essay.getEssayID().equalsIgnoreCase(essayID)) {
                return essay.getEssayPrompt();
            }
        }

        return "";
    }

    @Override
    public void addUser(CreateUserRequest createUserRequest) throws Exception {

        if (createUserRequest.getName() == null || createUserRequest.getName().isEmpty()) {
            throw new Exception("The name is missing");
        }

        if (createUserRequest.getEmail() == null || createUserRequest.getEmail().isEmpty()) {
            throw new Exception("The email is missing");
        }

        if (createUserRequest.getPassword() == null || createUserRequest.getPassword().length==0) {
            throw new Exception("The password is missing");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(java.nio.CharBuffer.wrap(createUserRequest.getPassword()));

        User user = new User(createUserRequest.getName(), createUserRequest.getEmail(), hashedPassword);
        userRepository.save(user);

    }


    @Override
    public void activateUser(InactiveUser inactiveUser) throws Exception {

        User user = new User(inactiveUser.getName(), inactiveUser.getEmail(), inactiveUser.getPassword());

        userRepository.save(user);

        emailService.addUserToAllUsersList(inactiveUser.getEmail(), inactiveUser.getName());

    }


    public boolean authenticateUser(User user, LoginRequest loginRequest) {

        return new BCryptPasswordEncoder().matches(java.nio.CharBuffer.wrap(loginRequest.getPassword()), user.getPassword());

    }

    public JSONObject getUserProfile(User user) throws Exception {

        JSONObject userJSON = user.toJSON();
        if (userJSON.has("schools")) {
            userJSON.remove("schools");
        }
        if (userJSON.has("userActivity")) {
            userJSON.remove("userActivity");
        }
        return userJSON;

    }


    public JSONObject getUserSchoolDetails(User user) throws Exception {

        JSONObject userJSON = user.toJSON();
        JSONArray schoolsArray = userJSON.getJSONArray("schools");
        for (int i = 0; i < schoolsArray.length(); i++) {
            JSONObject school = schoolsArray.getJSONObject(i);
            removeExtraJSONKey(school, "notes");
            removeExtraJSONKey(school, "essays");
            String schoolName = school.getString("shortName");
            SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(schoolName);
            if (schoolInfo != null) {
                school.put("name", schoolInfo.getName());
                school.put("location", schoolInfo.getLocation());
                school.put("round1_deadline", schoolInfo.getRound1Deadline());
                school.put("round2_deadline", schoolInfo.getRound2Deadline());
                school.put("round3_deadline", schoolInfo.getRound3Deadline());
                school.put("round4_deadline", schoolInfo.getRound4Deadline());
//                school.put("medianGMAT", schoolInfo.getMedianGMAT());
                school.put("avgGMAT", schoolInfo.getAvgGMAT());
                school.put("avgGPA", schoolInfo.getAvgGPA());
//                school.put("acceptanceRate", schoolInfo.getAcceptanceRate());
                school.put("logoURL", schoolInfo.getLogoURL());
                int numRequiredEssays = 0;
                for(SchoolInfoEssay essay : schoolInfo.getEssays()) {
                    boolean isRequired = essay.isRequired() == null ? false : Boolean.parseBoolean(essay.isRequired());
                    numRequiredEssays = isRequired ? numRequiredEssays+1 : numRequiredEssays;
                }
                int numEssays = 0;
                int numDrafts = 0;
                int numSentForReview = 0;
                int numReviewsReturned = 0;
                for (UserSchool userSchool : user.getSchools()) {
                    if (userSchool.getShortName().equalsIgnoreCase(schoolName)) {
                        for (Essay essay : userSchool.getEssays()) {
                            if (essay.getDrafts().size() > 0) {
                                numEssays = numEssays + 1;
                            }
                            numDrafts = numDrafts + essay.getDrafts().size();
                            for (EssayDraft draft : essay.getDrafts()) {
                                numSentForReview += draft.getReviews().size();
                                for(Review review : draft.getReviews()) {
                                    if(review.getReviewComments()!=null) {
                                        numReviewsReturned = numReviewsReturned + 1;
                                    }
                                }
                            }
                        }
                        school.put("numEssaysWorkedOn", numEssays);
                        school.put("numDrafts", numDrafts);
                        school.put("numDraftsSentForReview", numSentForReview);
                        school.put("numRequiredEssays", numRequiredEssays);
                        school.put("numReviewsReturned", numReviewsReturned);
                        school.put("numNotes", userSchool.getNotes().size());
                    }
                }
            }
        }

        userJSON = removeExtraJSONKey(userJSON, "teamPlayerExperience");
        userJSON = removeExtraJSONKey(userJSON, "whyMBA");
        userJSON = removeExtraJSONKey(userJSON, "failureExperience");
        userJSON = removeExtraJSONKey(userJSON, "leadershipExperience");
        userJSON = removeExtraJSONKey(userJSON, "whatDoYouBring");
        userJSON = removeExtraJSONKey(userJSON, "hobbiesOrInterests");
        userJSON = removeExtraJSONKey(userJSON, "longTermGoals");
        userJSON = removeExtraJSONKey(userJSON, "shortTermGoals");
        userJSON = removeExtraJSONKey(userJSON, "accomplishments");

        return userJSON;

    }


    private JSONObject removeExtraJSONKey(JSONObject json, String key) {
        if(json.has(key)){
            json.remove(key);
        }
        return json;
    }

    public void deleteSchool(User user, String schoolName) throws Exception {

        UserSchool userSchool = null;
        for (UserSchool school : user.getSchools()) {
            if (school.getShortName().equalsIgnoreCase(schoolName)) {
                userSchool = school;
                break;
            }
        }

        if (userSchool != null) {
            user.getSchools().remove(userSchool);
        }

        userRepository.save(user);
    }


    public void deleteRecommender(User user, String recommenderName) throws Exception {

        UserSchool userSchool = null;
        if (user.getRecommenders().contains(recommenderName)) {
            user.getRecommenders().remove(recommenderName);
        } else {
            throw new Exception("Did not find a recommender with the name " + recommenderName);
        }

        for (UserSchool school : user.getSchools()) {
            Recommendation recommendationToDelete = null;
            for (Recommendation recommendation : school.getRecommendations()) {
                if (recommendation.getRecommender().equalsIgnoreCase(recommenderName)) {
                    recommendationToDelete = recommendation;
                }
            }

            school.getRecommendations().remove(recommendationToDelete);

        }

        userRepository.save(user);

    }

    public void addEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, Keywords keywords) throws Exception {

        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        userSchool.addEssayDraft(essayDraftRequest, essayID, schoolInfo, keywords);
        userRepository.save(user);

    }

    public void addEssayDraftUpload(User user, UserSchool userSchool, MultipartFile file, String essayID) throws Exception {

        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        Essay essay = userSchool.getEssayForDraftUpload(essayID, schoolInfo);
        if(essay == null) {
            throw new Exception("Did not find essay with ID "+essayID);
        }

        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        DBObject dbObject = new BasicDBObject();
        GridFSFile gridFSID = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), dbObject);
        userSchool.addEssayDraftUpload(file.getOriginalFilename(), gridFSID.getId().toString(), essay, keywords, convFile);

        convFile.delete();
        userRepository.save(user);

    }


    public void addResume(User user, MultipartFile file) throws Exception {

        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        DBObject dbObject = new BasicDBObject();
        GridFSFile gridFSID = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), dbObject);
        String uploadID = gridFSID.getId().toString();
        Resume resume = new Resume();
//        int resumeNum = user.getResumes().size() + 1;
////        String resumeName = MessageFormat.format("{0}_Resume_{1}",user.getName().split("\\s+")[0], Integer.toString(resumeNum));
        resume.setResumeName(file.getOriginalFilename());
        resume.setUploadID(uploadID);

        user.addResume(resume);
        convFile.delete();

        userRepository.save(user);

    }



    public void addReviewDraft(User user, UserSchool userSchool, MultipartFile file, ReviewComments reviewComments) throws Exception {
        String gridFSID = addFileToMongo(file);
        reviewComments.setUploadID(gridFSID);
        userRepository.save(user);

    }


    public void addScores(User user, ScoreRequest scoreRequest) throws Exception {

        if(scoreRequest.getGmatScore()!=null) {
            user.setGmatScore(scoreRequest.getGmatScore());
        }

        if(scoreRequest.getTargetGmatScore()!=null) {
            user.setTargetGmatScore(scoreRequest.getTargetGmatScore());
        }

        if(scoreRequest.getGreScore()!=null) {
            user.setGreScore(scoreRequest.getGreScore());
        }

        if(scoreRequest.getTargetGreScore()!=null) {
            user.setTargetGreScore(scoreRequest.getTargetGreScore());
        }

        if(scoreRequest.getGpa()!=null) {
            user.setGpa(scoreRequest.getGpa());
        }

        saveUser(user);

    }

    public JSONObject getScores(User user) throws Exception {

        JSONObject schoolsInfoJSON = new JSONObject();
        List<UserSchool> userSchools = user.getSchools();

        for(UserSchool school : userSchools) {
            SchoolInfo info =  schoolInfoRepository.findByShortName(school.getShortName());
            JSONObject schoolInfoJSON = new JSONObject();
            schoolInfoJSON.put("AvgGMAT", info.getAvgGMAT());
//            schoolInfoJSON.put("MedianGMAT", info.getMedianGMAT());
            schoolInfoJSON.put("AvgGPA", info.getAvgGPA());
            schoolsInfoJSON.put(info.getName(), schoolInfoJSON);
        }

        JSONObject userJSON = new JSONObject();
        userJSON.put("gpa", user.getGpa());
        userJSON.put("gmatScore", user.getGmatScore());
        userJSON.put("targetGmatScore", user.getTargetGmatScore());
        userJSON.put("greScore", user.getTargetGreScore());
        userJSON.put("targetGreScore", user.getTargetGreScore());
        userJSON.put("schools", schoolsInfoJSON);

        return userJSON;
    }


    public void forgotPassword(User user) throws Exception {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String resetCode = RandomStringUtils.random( 10, characters );

        emailService.sendForgotPasswordEmail(emailSender, user, resetCode);
        user.setPasswordResetCode(resetCode);
        saveUser(user);

    }

    public void changePassword(User user, char[] password, boolean resetPasswordResetCode) throws Exception {
        String hashedPassword = new BCryptPasswordEncoder().encode(java.nio.CharBuffer.wrap(password));
        user.setPassword(hashedPassword);
        if(resetPasswordResetCode) {
            user.setPasswordResetCode("");
        }
        saveUser(user);
    }



    private String addFileToMongo(MultipartFile file) throws Exception{
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        DBObject dbObject = new BasicDBObject();
        GridFSFile gridFSID = gridFsTemplate.store(file.getInputStream(), dbObject);
        convFile.delete();
        return gridFSID.getId().toString();

    }

    public ByteArrayOutputStream getResumeUpload(User user, Resume resume) throws Exception {
        GridFSDBFile gridfsfile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resume.getUploadID())));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridfsfile.writeTo(outputStream);

        return outputStream;

    }


    public Resume getResume(User user, String resumeID) throws Exception {
        for(Resume resume : user.getResumes()) {
            if(resume.getResumeID().equalsIgnoreCase(resumeID)) {
                return resume;
            }
        }

        return null;
    }




    public ByteArrayOutputStream getEssayDraftUploaded(User user, UserSchool userSchool, String essayID, String draftID) throws Exception {

        EssayDraft draft = userSchool.getEssayDraft(essayID, draftID);

        if(draft.getUploadID()==null) {
            throw new Exception("This draft was not uploaded - does not have an upload ID");
        }

        GridFSDBFile gridfsfile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(draft.getUploadID())));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridfsfile.writeTo(outputStream);

        return outputStream;
    }


    public ByteArrayOutputStream getFileUploaded(String id) throws Exception{
        GridFSDBFile gridfsfile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridfsfile.writeTo(outputStream);
        return outputStream;
    }

    public ByteArrayOutputStream getResumeForDownload(Resume resume) throws Exception{
        GridFSDBFile gridfsfile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resume.getUploadID())));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridfsfile.writeTo(outputStream);
        return outputStream;
    }

    public File getDraft(User user, UserSchool userSchool, String essayID, String draftID) throws Exception {

        EssayDraft draft = userSchool.getEssayDraft(essayID, draftID);

        File draftFile = new File(draft.getDraftName());

        if(draft.getUploadID()==null) {
            String contents = draft.getContents();
            FileUtils.writeStringToFile(draftFile, contents, "UTF-8");
            return draftFile;
        }
        else {
            ByteArrayOutputStream contentsStream = getEssayDraftUploaded(user, userSchool, essayID, draftID);
            FileOutputStream outputStream = new FileOutputStream(draftFile);
            contentsStream.writeTo(outputStream);
            return draftFile;
        }
    }


    public void setDeadlineForSchool(User user, UserSchool userSchool, String deadline) throws Exception {

        userSchool.setDeadline(deadline);
        userRepository.save(user);
    }




    public void updateUserProfile(User user, ProfileRequest profileRequest) throws Exception {

        if (profileRequest.getWhyMBA() != null) {
            user.setWhyMBA(profileRequest.getWhyMBA());
        }

        if (profileRequest.getShortTermGoals() != null) {
            user.setShortTermGoals(profileRequest.getShortTermGoals());
        }

        if (profileRequest.getLongTermGoals() != null) {
            user.setLongTermGoals(profileRequest.getLongTermGoals());
        }

        if (profileRequest.getAccomplishments() != null) {
            user.setAccomplishments(profileRequest.getAccomplishments());
        }

        if (profileRequest.getLeadershipExperience() != null) {
            user.setLeadershipExperience(profileRequest.getLeadershipExperience());
        }

        if (profileRequest.getTeamPlayerExperience() != null) {
            user.setTeamPlayerExperience(profileRequest.getTeamPlayerExperience());
        }

        if (profileRequest.getFailureExperience() != null) {
            user.setFailureExperience(profileRequest.getFailureExperience());
        }

        if (profileRequest.getAccomplishments() != null) {
            user.setAccomplishments(profileRequest.getAccomplishments());
        }

        if (profileRequest.getHobbiesOrInterests() != null) {
            user.setHobbiesOrInterests(profileRequest.getHobbiesOrInterests());
        }

        if (profileRequest.getWhatDoYouBring() != null) {
            user.setWhatDoYouBring(profileRequest.getWhatDoYouBring());
        }

        userRepository.save(user);

    }


    public void updateEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID, HashMap<String, List<String>> schoolKeywords) throws Exception {

        userSchool.updateEssayDraft(essayDraftRequest, essayID, draftID, schoolKeywords);
        userRepository.save(user);

    }

    public void addNote(User user, UserSchool userSchool, NotesRequest notesRequest) throws Exception {

        userSchool.addNote(notesRequest);
        userRepository.save(user);
    }

    public void updateNote(User user, UserSchool userSchool, NotesRequest notesRequest, String noteID) throws Exception {

        userSchool.updateNote(notesRequest, noteID);
        userRepository.save(user);
    }

    public JSONObject getRecommender(User user, UserSchool userSchool,
                               String recommenderID) throws Exception {

        return userSchool.getRecommender(recommenderID).toJSON();

    }

    public void updateRecommender(User user, UserSchool userSchool, RecommenderRequest recommenderRequest,
                                  String recommenderID) throws Exception {


        userSchool.updateRecommender(recommenderRequest, recommenderID);
        userRepository.save(user);

    }


    public void deleteNote(User user, UserSchool userSchool, String noteID) throws Exception {

        userSchool.deleteNote(noteID);
        userRepository.save(user);

    }

    public JSONObject getNote(User user, UserSchool userSchool, String noteID) throws Exception {

        Note note = userSchool.getNote(noteID);

        return note.toJSON();
    }


    public void deleteEssayDraft(User user, UserSchool userSchool, String essayID, String draftID) throws Exception {
        EssayDraft essayDraft = userSchool.getEssayDraft(essayID, draftID);
        if(essayDraft.getUploadID()!=null && !essayDraft.getUploadID().isEmpty() &&
                essayDraft.getDraftType()== EssayDraft.DraftType.UPLOAD){
            gridFsTemplate.delete(new Query(Criteria.where("_id").is(essayDraft.getUploadID())));
        }
        userSchool.deleteEssayDraft(essayID, draftID);
        userRepository.save(user);
    }

    public void deleteResume(User user, Resume resume) throws Exception {

        gridFsTemplate.delete(new Query(Criteria.where("_id").is(resume.getUploadID())));
        user.getResumes().remove(resume);
        userRepository.save(user);

    }


    public JSONObject getEssay(User user, UserSchool userSchool, String essayID) throws Exception {
        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        String essayPrompt = "";
        for (SchoolInfoEssay essay : schoolInfo.getEssays()) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayPrompt = essay.getEssayPrompt();
            }
        }
        return userSchool.getEssay(essayID, essayPrompt);

    }

    public void updateEssayStatus(User user, UserSchool userSchool, String essayID, EssayStatusRequest essayRequest) throws Exception {
        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        userSchool.updateEssayStatus(essayRequest, essayID, schoolInfo);
        userRepository.save(user);
    }


    public JSONObject getUserSchoolDetail(User user, UserSchool userSchool) throws Exception {
        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        JSONObject userSchoolJSON = userSchool.toJSON(schoolInfo.getEssays());
        userSchoolJSON.put("logoURL", schoolInfo.getLogoURL());
        userSchoolJSON.put("name", schoolInfo.getName());
        userSchoolJSON.put("location", schoolInfo.getLocation());

        JSONObject schoolJSON = schoolInfo.toJSON();
        Iterator<?> keys = schoolJSON.keys();

        while(keys.hasNext()){
            String key = (String)keys.next();
            if(!key.equalsIgnoreCase("essays")){
                userSchoolJSON.put(key, schoolJSON.get(key));
            }
        }

        return userSchoolJSON;
    }


    @Override
    public void addSchools(AddSchoolsRequest userRequest, User user) throws Exception {

        if (userRequest.getSchools() != null) {
            updateSchools(userRequest, user);
        }
        userRepository.save(user);
    }


    @Override
    public void addRecommenders(AddRecommendersRequest request, User user) throws Exception {

        if (request.getRecommenders() != null) {
            for (String recommender : request.getRecommenders()) {
                if (user.getRecommenders().contains(recommender)) {
                    throw new Exception("A recommender by the name " + recommender + " already exists - please use a " +
                            "different name");
                }
                user.getRecommenders().add(recommender);
                //Update the schools with this recommender
                for (UserSchool school : user.getSchools()) {
                    school.addRecommendation(new Recommendation(recommender));
                }
            }
        }
        userRepository.save(user);
    }


    private void updateSchools(AddSchoolsRequest userRequest, User user) throws Exception {

        //Update school
        for (String schoolName : userRequest.getSchools()) {

            boolean alreadyAdded = false;
            //Check if school is already added
            for (UserSchool school : user.getSchools()) {
                if (school.getShortName().equalsIgnoreCase(schoolName)) {
                    alreadyAdded = true;
                    break;
                }
            }

            if (schoolInfoRepository.findByShortName(schoolName) == null) {
                throw new Exception("Did not find a school with the name [" + schoolName + "] in our DB");
            }

            if (!alreadyAdded) {
                UserSchool school = new UserSchool(schoolName);
                user.addSchool(school);
            }
        }


    }
}

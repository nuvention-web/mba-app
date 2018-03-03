package mbaapp.mongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import mbaapp.core.Essay;
import mbaapp.core.EssayDraft;
import mbaapp.core.Keywords;
import mbaapp.core.Note;
import mbaapp.core.SchoolInfoEssay;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.core.Recommendation;
import mbaapp.core.SchoolInfo;
import mbaapp.requests.EssayStatusRequest;
import mbaapp.requests.AddRecommendersRequest;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.AddSchoolsRequest;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import mbaapp.requests.NotesRequest;
import mbaapp.requests.ProfileRequest;
import mbaapp.requests.RecommenderRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
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



    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public void addUser(CreateUserRequest createUserRequest) throws Exception {

        if (createUserRequest.getName() == null || createUserRequest.getName().isEmpty()) {
            throw new Exception("The name is missing");
        }

        if (createUserRequest.getEmail() == null || createUserRequest.getEmail().isEmpty()) {
            throw new Exception("The email is missing");
        }

        User user = new User(createUserRequest.getName(), createUserRequest.getEmail());
        userRepository.save(user);

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
                school.put("medianGMAT", schoolInfo.getMedianGMAT());
                school.put("avgGMAT", schoolInfo.getAvgGMAT());
                school.put("avgGPA", schoolInfo.getAvgGPA());
                school.put("acceptanceRate", schoolInfo.getAcceptanceRate());
                school.put("logoURL", schoolInfo.getLogoURL());
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
        GridFSFile gridFSID = gridFsTemplate.store(file.getInputStream(), dbObject);
        userSchool.addEssayDraftUpload(file.getOriginalFilename(), gridFSID.getId().toString(), essay, keywords, convFile);

        convFile.delete();
        userRepository.save(user);

    }

    public ByteArrayOutputStream downloadDraft(User user, UserSchool userSchool, String essayID, String draftID) throws Exception {

        EssayDraft draft = userSchool.getEssayDraft(essayID, draftID);

        if(draft.getUploadID()==null) {
            throw new Exception("This draft was not uploaded - does not have an upload ID");
        }

        GridFSDBFile gridfsfile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(draft.getUploadID())));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gridfsfile.writeTo(outputStream);

        return outputStream;
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


    public void deleteEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID) throws Exception {
        userSchool.deleteEssayDraft(essayDraftRequest, essayID, draftID);
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

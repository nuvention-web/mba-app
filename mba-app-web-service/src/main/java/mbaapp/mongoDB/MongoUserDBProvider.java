package mbaapp.mongoDB;

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
import mbaapp.requests.ProfileRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jnag on 2/15/18.
 */
@Service("userMongoDB")
public class MongoUserDBProvider implements UserDBProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;


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

    public void addEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID) throws Exception {

        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        userSchool.addEssayDraft(essayDraftRequest, essayID, schoolInfo);
        userRepository.save(user);

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


    public void updateEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID) throws Exception {

        userSchool.updateEssayDraft(essayDraftRequest, essayID, draftID);
        userRepository.save(user);

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
        return userSchool.toJSON(schoolInfo.getEssays());
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

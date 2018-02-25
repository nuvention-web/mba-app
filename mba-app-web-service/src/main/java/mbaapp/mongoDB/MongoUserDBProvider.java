package mbaapp.mongoDB;

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

    public JSONObject getUserDetails(User user) throws Exception {

        JSONObject userJSON = user.toJSON();
        JSONArray schoolsArray = userJSON.getJSONArray("schools");
        for (int i = 0; i < schoolsArray.length(); i++) {
            JSONObject school = schoolsArray.getJSONObject(i);
            String schoolName = school.getString("shortName");
            SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(schoolName);
            if (schoolInfo != null) {
                school.put("name", schoolInfo.getName());
                school.put("location", schoolInfo.getLocation());
                school.put("round1_deadline", schoolInfo.getRound1Deadline());
                school.put("round2_deadline", schoolInfo.getRound2Deadline());
                school.put("round3_deadline", schoolInfo.getRound3Deadline());
                school.put("round4_deadline", schoolInfo.getRound4Deadline());
            }
        }

        return userJSON;

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
        if(user.getRecommenders().contains(recommenderName)){
            user.getRecommenders().remove(recommenderName);
        }
        else {
            throw new Exception("Did not find a recommender with the name "+recommenderName);
        }

        for(UserSchool school : user.getSchools()) {
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


    public void updateEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID) throws Exception {

        userSchool.updateEssayDraft(essayDraftRequest, essayID);
        userRepository.save(user);

    }

    public void deleteEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID) throws Exception {
        userSchool.deleteEssayDraft(essayDraftRequest, essayID);
        userRepository.save(user);
    }


    public void updateEssayStatus(User user, UserSchool userSchool, String essayID, EssayStatusRequest essayRequest) throws Exception {
        SchoolInfo schoolInfo = schoolInfoRepository.findByShortName(userSchool.getShortName());
        userSchool.updateEssayStatus(essayRequest, essayID, schoolInfo);
        userRepository.save(user);
    }


    public JSONObject getUserSchoolDetail(User user, UserSchool userSchool) throws Exception{
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

        if(request.getRecommenders()!=null) {
            for(String recommender : request.getRecommenders()){
                if(user.getRecommenders().contains(recommender)) {
                    throw new Exception("A recommender by the name " + recommender +" already exists - please use a " +
                            "different name");
                }
                user.getRecommenders().add(recommender);
                //Update the schools with this recommender
                for(UserSchool school : user.getSchools()) {
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

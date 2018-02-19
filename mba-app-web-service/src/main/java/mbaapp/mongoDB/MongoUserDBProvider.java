package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.UpdateUserRequest;
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


    @Override
    public void updateUser(UpdateUserRequest userRequest, User user) throws Exception {

        if (userRequest.getSchools() != null) {
            updateSchools(userRequest, user);
        }
        userRepository.save(user);
    }


    private void updateSchools(UpdateUserRequest userRequest, User user) throws Exception {

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

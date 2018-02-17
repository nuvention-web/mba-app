package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
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
    public void addUser(JSONObject payload) throws Exception {
        String name = payload.optString("name");
        String email = payload.optString("email");

        if(name.isEmpty()){
            throw new Exception("The name is missing");
        }

        if(email.isEmpty()){
            throw new Exception("The email is missing");
        }

        User user = new User(name, email);
        userRepository.save(user);

    }

    public JSONObject getUserDetails(User user) throws Exception{

        JSONObject userJSON = user.toJSON();
        JSONArray schoolsArray = userJSON.getJSONArray("schools");
        for(int i=0; i<schoolsArray.length(); i++) {
            JSONObject school = schoolsArray.getJSONObject(i);
            String schoolName = school.getString("name");
            SchoolInfo schoolInfo = schoolInfoRepository.findByName(schoolName);
            if(schoolInfo!=null) {
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
        for(UserSchool school : user.getSchools()) {
            if(school.getName().equalsIgnoreCase(schoolName)){
                userSchool = school;
                break;
            }
        }

        if(userSchool!=null){
            user.getSchools().remove(userSchool);
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(JSONObject payload) throws Exception {

    }


    @Override
    public void addSchool(JSONObject payload, User user) throws Exception {

        JSONArray schools = payload.getJSONArray("schools");
        for(int i=0; i<schools.length(); i++) {

            boolean alreadyAdded = false;
            String schoolName = schools.getString(i);
            //Check if school is already added
            for(UserSchool school : user.getSchools()){
                if(school.getName().equalsIgnoreCase(schoolName)){
                    alreadyAdded = true;
                    break;
                }
            }

            if(schoolInfoRepository.findByName(schoolName)==null) {
                throw new Exception("Did not find a school with the name ["+schoolName + "] in our DB");
            }

            if(!alreadyAdded) {
                UserSchool school = new UserSchool(schoolName);
                user.addSchool(school);
            }
        }

        userRepository.save(user);
    }
}

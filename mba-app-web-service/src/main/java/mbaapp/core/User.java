package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/15/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    public String id;
    public String name;
    public String email;
    //@JsonIgnore
    public String password;
    public List<String> recommenders;
    public List<UserSchool> schools;
    public List<Activity> userActivity;

    public String whyMBA;
    public String shortTermGoals;
    public String longTermGoals;

    public List<String> leadershipExperience;
    public List<String> teamPlayerExperience;
    public List<String> failureExperience;

    public List<String> accomplishments;
    public List<String> hobbiesOrInterests;

    public String whatDoYouBring;

    public User(){

    }

    public User(String name, String email, String password) {
//        userID = UUID.randomUUID().toString();
        this.password = password;
        this.name = name;
        this.email = email;
        schools = new ArrayList<>();
        recommenders = new ArrayList<>();
        userActivity = new ArrayList<>();
        leadershipExperience  = new ArrayList<>();
        teamPlayerExperience = new ArrayList<>();
        failureExperience = new ArrayList<>();
        accomplishments = new ArrayList<>();
        hobbiesOrInterests = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setRecommenders(List<String> recommenders) {
        this.recommenders = recommenders;
    }

    public void setUserActivity(List<Activity> userActivity) {
        this.userActivity = userActivity;
    }

    public void setWhyMBA(String whyMBA) {
        this.whyMBA = whyMBA;
    }

    public void setShortTermGoals(String shortTermGoals) {
        this.shortTermGoals = shortTermGoals;
    }

    public void setLongTermGoals(String longTermGoals) {
        this.longTermGoals = longTermGoals;
    }

    public void setLeadershipExperience(List<String> leadershipExperience) {
        this.leadershipExperience = new ArrayList<>(leadershipExperience);
    }

    public void setTeamPlayerExperience(List<String> teamPlayerExperience) {
        this.teamPlayerExperience = new ArrayList<>(teamPlayerExperience);
    }

    public void setFailureExperience(List<String> failureExperience) {
        this.failureExperience = new ArrayList<>(failureExperience);
    }

    public void setAccomplishments(List<String> accomplishments) {
        this.accomplishments = new ArrayList<>(accomplishments);
    }

    public void setHobbiesOrInterests(List<String> hobbiesOrInterests) {
        this.hobbiesOrInterests = new ArrayList<>(hobbiesOrInterests);
    }

    public void setWhatDoYouBring(String whatDoYouBring) {
        this.whatDoYouBring = whatDoYouBring;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRecommenders() {
        return recommenders;
    }

    public void addActivity(String message) {
        userActivity.add(new Activity(message));
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserSchool> getSchools() {
        return schools;
    }

    public String getPassword() {
        return password;
    }

    public void setSchools(List<UserSchool> schools) {
        this.schools = schools;
    }

    public void addSchool(UserSchool school) {
        this.schools.add(school);
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject userJSON = new JSONObject(stringWriter.toString());
        if(userJSON.has("password")){
            userJSON.remove("password");
        }
        return userJSON;

    }
}

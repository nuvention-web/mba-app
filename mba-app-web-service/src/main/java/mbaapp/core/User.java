package mbaapp.core;

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
    public List<String> recommenders;
    public List<UserSchool> schools;
    public List<Activity> userActivity;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        schools = new ArrayList<>();
        recommenders = new ArrayList<>();
        userActivity = new ArrayList<>();
    }

    public String getId() {
        return id;
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
        return userJSON;

    }
}

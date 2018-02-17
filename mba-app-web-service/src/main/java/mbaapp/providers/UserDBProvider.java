package mbaapp.providers;

import mbaapp.core.SchoolInfo;
import mbaapp.core.User;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jnag on 2/14/18.
 */
public interface UserDBProvider {

    public User getUser(String email);

    public void addUser(JSONObject payload) throws Exception;

    public void updateUser(JSONObject payload) throws Exception;

    public void addSchool(JSONObject payload, User user) throws Exception;

    public JSONObject getUserDetails(User user) throws Exception;

    public void deleteSchool(User user, String schoolName) throws Exception;

}
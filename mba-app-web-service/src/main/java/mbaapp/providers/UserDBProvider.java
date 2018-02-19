package mbaapp.providers;

import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.UpdateUserRequest;
import mbaapp.core.User;
import org.json.JSONObject;

/**
 * Created by jnag on 2/14/18.
 */
public interface UserDBProvider {

    public User getUser(String email);

    public void addUser(CreateUserRequest createUserRequest) throws Exception;

    public void updateUser(UpdateUserRequest request, User user) throws Exception;

    public JSONObject getUserDetails(User user) throws Exception;

    public void deleteSchool(User user, String schoolName) throws Exception;

}
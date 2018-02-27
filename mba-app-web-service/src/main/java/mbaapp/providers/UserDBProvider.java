package mbaapp.providers;

import mbaapp.requests.EssayDraftRequest;
import mbaapp.core.UserSchool;
import mbaapp.requests.EssayStatusRequest;
import mbaapp.requests.AddRecommendersRequest;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.AddSchoolsRequest;
import mbaapp.core.User;
import mbaapp.requests.ProfileRequest;
import org.json.JSONObject;

/**
 * Created by jnag on 2/14/18.
 */
public interface UserDBProvider {

    public User getUser(String email);

    public void addUser(CreateUserRequest createUserRequest) throws Exception;

    public void addSchools(AddSchoolsRequest request, User user) throws Exception;

    public void addRecommenders(AddRecommendersRequest request, User user) throws Exception;

    public JSONObject getUserSchoolDetails(User user) throws Exception;

    public JSONObject getUserProfile(User user) throws Exception;

    public void deleteSchool(User user, String schoolName) throws Exception;

    public void deleteRecommender(User user, String recommenderName) throws Exception;

    public void updateEssayStatus(User user, UserSchool userSchool, String essayID, EssayStatusRequest addEssayRequest) throws Exception;

    public JSONObject getEssay(User user, UserSchool userSchool, String essayID) throws Exception;

    public void addEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID) throws Exception;

    public void updateEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID) throws Exception;

    public void deleteEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID) throws Exception;

    public JSONObject getUserSchoolDetail(User user, UserSchool userSchool) throws Exception;

    public void updateUserProfile(User user, ProfileRequest profileRequest) throws Exception;

}
package mbaapp.providers;

import mbaapp.core.Keywords;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.core.UserSchool;
import mbaapp.requests.EssayStatusRequest;
import mbaapp.requests.AddRecommendersRequest;
import mbaapp.requests.CreateUserRequest;
import mbaapp.requests.AddSchoolsRequest;
import mbaapp.core.User;
import mbaapp.requests.NotesRequest;
import mbaapp.requests.ProfileRequest;
import mbaapp.requests.RecommenderRequest;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

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

    public void addEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, Keywords keywords) throws Exception;

    public void addEssayDraftUpload(User user, UserSchool userSchool, MultipartFile file, String essayID) throws Exception;

    public void addNote(User user, UserSchool userSchool, NotesRequest notesRequest) throws Exception;

    public void updateNote(User user, UserSchool userSchool, NotesRequest notesRequest, String noteID) throws Exception;

    public JSONObject getRecommender(User user, UserSchool userSchool, String recommenderID) throws Exception;

    public void updateRecommender(User user, UserSchool userSchool, RecommenderRequest recommenderRequest, String recommenderID) throws Exception;

    public void deleteNote(User user, UserSchool userSchool, String noteID) throws Exception;

    public JSONObject getNote(User user, UserSchool userSchool, String noteID) throws Exception;

    public void updateEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID, HashMap<String, List<String>> schoolKeywords) throws Exception;

    public void deleteEssayDraft(User user, UserSchool userSchool, EssayDraftRequest essayDraftRequest, String essayID, String draftID) throws Exception;

    public JSONObject getUserSchoolDetail(User user, UserSchool userSchool) throws Exception;

    public void updateUserProfile(User user, ProfileRequest profileRequest) throws Exception;

    public void setDeadlineForSchool(User user, UserSchool userSchool, String deadline) throws Exception;

    public ByteArrayOutputStream downloadDraft(User user, UserSchool school, String essayID, String draftID) throws Exception;
}
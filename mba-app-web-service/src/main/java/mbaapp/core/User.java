package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
    public List<Resume> resumes;
    public String greScore;
    public String gmatScore;
    public String targetGreScore;
    public String targetGmatScore;
    public String gpa;
    public String passwordResetCode;
    public String lastLogin;

    public List<Task> tasks;
    public List <Deadline> deadlines;

    public ProfilePDF profileRecommendation;

    public String whatDoYouBring;

    public int resumesScored;

    public boolean allowUnlimitedResumes = false;

    public User(){

    }

    public ProfilePDF getProfilePDF() {
        return profileRecommendation;
    }

    public void setProfilePDF(ProfilePDF profileRecommendation) {
        this.profileRecommendation = profileRecommendation;
    }

    public void addTask(String taskName, String details, String date) {
        Task task = new Task(taskName, details, date);
        if(tasks == null) {
            tasks = new ArrayList<>();
        }

        tasks.add(task);
    }

    public int getResumesScored() {
        return resumesScored;
    }

    public void setResumesScored(int resumesScored) {
        this.resumesScored = resumesScored;
    }

    public void setAllowUnlimitedResumes(boolean allowUnimitedResumes) {
        this.allowUnlimitedResumes = allowUnimitedResumes;
    }

    public boolean isAllowUnlimitedResumes() {
        return allowUnlimitedResumes;
    }

    public void deleteTask(String taskID) throws Exception {

        Task taskToDelete = null;
        for(Task task : tasks) {
            if(task.getId().equalsIgnoreCase(taskID)) {
                taskToDelete = task;
                break;
            }
        }

        if(taskToDelete!=null) {
            tasks.remove(taskToDelete);
        }
        else{
            throw new Exception("Did not find task");
        }
    }

    public void updateTask(String taskID, String taskName, String details, String date) throws Exception{


        Task taskToUpdate = null;
        for(Task task : tasks) {
            if(task.getId().equalsIgnoreCase(taskID)) {
                taskToUpdate = task;
                task.setName(taskName);
                task.setDetails(details);
                task.setDate(date);
                break;
            }
        }

        if(taskToUpdate==null) {
            throw new Exception("Did not find task");
        }

    }

    public void completedTask(String taskID) throws Exception{


        Task taskToUpdate = null;
        for(Task task : tasks) {
            if(task.getId().equalsIgnoreCase(taskID)) {
                taskToUpdate = task;
                if(task.isCompleted()) {
                    task.setCompleted(false);
                }
                else if(!task.isCompleted()) {
                    task.setCompleted(true);
                }
                break;
            }
        }

        if(taskToUpdate==null) {
            throw new Exception("Did not find task");
        }

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
        resumes = new ArrayList<>();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordResetCode(String code){
        this.passwordResetCode = code;
    }

    public String getPasswordResetCode() {
        return this.passwordResetCode;
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

    public List<Resume> getResumes(){
        if(resumes==null) {
            resumes = new ArrayList<>();
        }
        return resumes;
    }

    public void addResume(Resume resume) {
        if(resumes == null) {
            resumes = new ArrayList<>();
        }

        resumes.add(resume);
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getGreScore() {
        return greScore;
    }

    public String getGmatScore() {
        return gmatScore;
    }

    public String getTargetGreScore() {
        return targetGreScore;
    }

    public String getTargetGmatScore() {
        return targetGmatScore;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public void setGreScore(String greScore) {
        this.greScore = greScore;
    }

    public void setGmatScore(String gmatScore) {
        this.gmatScore = gmatScore;
    }

    public void setTargetGreScore(String targetGreScore) {
        this.targetGreScore = targetGreScore;
    }

    public void setTargetGmatScore(String targetGmatScore) {
        this.targetGmatScore = targetGmatScore;
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

    public String getWhyMBA() {
        return whyMBA;
    }

    public String getShortTermGoals() {
        return shortTermGoals;
    }

    public String getLongTermGoals() {
        return longTermGoals;
    }

    public List<String> getLeadershipExperience() {
        return leadershipExperience;
    }

    public List<String> getTeamPlayerExperience() {
        return teamPlayerExperience;
    }

    public List<String> getFailureExperience() {
        return failureExperience;
    }

    public List<String> getAccomplishments() {
        return accomplishments;
    }

    public List<String> getHobbiesOrInterests() {
        return hobbiesOrInterests;
    }

    public List<Task> getTasks() {
        Collections.sort(tasks);
        return tasks;
    }

    public List<Deadline> getDeadlines() {
        return deadlines;
    }

    public String getWhatDoYouBring() {
        return whatDoYouBring;
    }
}

package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jnag on 2/26/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileRequest {

    public String whyMBA;
    public String shortTermGoals;
    public String longTermGoals;

    public List<String> leadershipExperience;
    public List<String> teamPlayerExperience;
    public List<String> failureExperience;

    public List<String> accomplishments;
    public List<String> hobbiesOrInterests;

    public String whatDoYouBring;

    public ProfileRequest(){

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
        this.leadershipExperience = leadershipExperience;
    }

    public void setTeamPlayerExperience(List<String> teamPlayerExperience) {
        this.teamPlayerExperience = teamPlayerExperience;
    }

    public void setFailureExperience(List<String> failureExperience) {
        this.failureExperience = failureExperience;
    }

    public void setAccomplishments(List<String> accomplishments) {
        this.accomplishments = accomplishments;
    }

    public void setHobbiesOrInterests(List<String> hobbiesOrInterests) {
        this.hobbiesOrInterests = hobbiesOrInterests;
    }

    public void setWhatDoYouBring(String whatDoYouBring) {
        this.whatDoYouBring = whatDoYouBring;
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

    public String getWhatDoYouBring() {
        return whatDoYouBring;
    }
}

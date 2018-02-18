package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/15/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSchool{

    private String recommendations;
    private String essays;
    private List<String> notes;
    private String shortName;

    public String getShortName() {
        return shortName;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getEssays() {
        return essays;
    }

    public void setEssays(String essays) {
        this.essays = essays;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }


    public UserSchool(){

    }

    public UserSchool(String name){
        recommendations = "";
        essays= "";
        notes = new ArrayList<>();
        this.shortName = name;
    }
}

package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.requests.EssayStatusRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/15/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSchool{

    private List<Recommendation> recommendations;
    private List<Essay> essays;
    private List<String> notes;
    private String shortName;

    public String getShortName() {
        return shortName;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public List<Essay> getEssays() {
        return essays;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public void addRecommendation(Recommendation recommendation) {
        if(this.recommendations == null ) {
            this.recommendations = new ArrayList<>();
        }
        this.recommendations.add(recommendation);
    }

    public UserSchool(){

    }


    public void updateEssayStatus(EssayStatusRequest essayRequest, String essayID, SchoolInfo schoolInfo) throws Exception{

        boolean essayNotFound = true;
        for(Essay essay : essays) {
            if(essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayNotFound = false;
                if(essayRequest.getEssayStatus()!=null) {
                    essay.setStatus(essayRequest.getEssayStatus());
                }
            }
        }

        if(essayNotFound){
            for(SchoolInfoEssay schoolInfoEssay : schoolInfo.getEssays()) {
                if(essayID.equalsIgnoreCase(schoolInfoEssay.getEssayID())) {
                    Essay essay = new Essay(essayID, essayRequest.getEssayStatus());
                    essays.add(essay);
                    essayNotFound = false;
                }
            }
        }

        if(essayNotFound) {
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }
    }



    public void addEssayDraft(EssayDraftRequest request, String essayID, SchoolInfo schoolInfo) throws Exception{

        boolean essayNotFound = true;
        for(Essay essay : essays) {
            if(essay.getEssayID().equalsIgnoreCase(essayID)){
                essayNotFound = false;
                essay.addDraft(request.getDraftName(), request.getContents());
            }
        }

        if(essayNotFound){
            for(SchoolInfoEssay schoolInfoEssay : schoolInfo.getEssays()) {
                if(essayID.equalsIgnoreCase(schoolInfoEssay.getEssayID())) {
                    Essay essay = new Essay(essayID, request.getEssayStatus());
                    essay.addDraft(request.getDraftName(), request.getContents());
                    essays.add(essay);
                    essayNotFound = false;
                }
            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

    }

    public JSONObject getEssay(String essayID, String essayPrompt) throws Exception{
        Essay essayRequested = null;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayRequested = essay;
            }
        }

        if(essayRequested == null){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

        JSONObject essayJSON = essayRequested.toJSON();
        essayJSON.put("prompt", essayPrompt);
        return essayJSON;

    }

    public void deleteEssayDraft(EssayDraftRequest request, String essayID, String draftID) throws Exception {
        boolean essayNotFound = true;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayNotFound = false;
                essay.deleteDraft(draftID);
            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

    }


    public void updateEssayDraft(EssayDraftRequest request, String essayID, String draftID) throws Exception {
        boolean essayNotFound = true;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                if(request.getContents()!=null) {
                    essay.updateDraftContents(draftID, request.getContents(), request.getUrl());
                    essayNotFound = false;
                }

            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

    }

    public JSONObject toJSON(List<SchoolInfoEssay> schoolInfoEssays) throws Exception{

        List<SchoolInfoEssay> schoolEssaysCopy = new ArrayList<>(schoolInfoEssays);

        for(Essay essay : this.essays){
            SchoolInfoEssay essayContained = null;
            for(SchoolInfoEssay schoolInfoEssay : schoolEssaysCopy) {
                if(schoolInfoEssay.getEssayID().equalsIgnoreCase(essay.getEssayID())){
                    essayContained = schoolInfoEssay;
                    break;
                }
            }
            if(essayContained!=null) {
                schoolEssaysCopy.remove(essayContained);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject userJSON = new JSONObject(stringWriter.toString());

        JSONArray essaysJSON = userJSON.getJSONArray("essays");
        for(int i=0; i < essaysJSON.length(); i++) {
            JSONObject essayJSON = essaysJSON.getJSONObject(i);
            for(SchoolInfoEssay essay : schoolInfoEssays){
                if(essayJSON.getString("essayID").equalsIgnoreCase(essay.getEssayID())){
                    essayJSON.put("prompt", essay.getEssayPrompt());
                }
                if(essayJSON.has("drafts")) {
                    essayJSON.remove("drafts");
                }
            }
        }

        for(SchoolInfoEssay essay : schoolEssaysCopy){
            JSONObject essayJSON = new JSONObject();
            essayJSON.put("essayID", essay.getEssayID());
            essayJSON.put("prompt", essay.getEssayPrompt());
            essayJSON.put("status", Essay.EssayStatus.NOT_STARTED);
            essaysJSON.put(essayJSON);

        }


        return userJSON;
    }

    public UserSchool(String name){
        recommendations = new ArrayList<>();
        notes = new ArrayList<>();
        this.shortName = name;
        essays = new ArrayList<>();
    }
}

package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import mbaapp.requests.EssayDraftRequest;
import mbaapp.requests.EssayStatusRequest;
import mbaapp.requests.NotesRequest;
import mbaapp.requests.RecommenderRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by jnag on 2/15/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSchool{

    private List<Recommendation> recommendations;
    private List<Essay> essays;
    private List<Note> notes;
    private String shortName;
    private String deadline;
    private Set<String> warningWords;

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

    public List<Note> getNotes() {
        return notes;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setNotes(List<Note> notes) {
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


    public void addEssayDraft(EssayDraftRequest request, String essayID, SchoolInfo schoolInfo, Keywords keywords) throws Exception{

        boolean essayNotFound = true;
        for(Essay essay : essays) {
            if(essay.getEssayID().equalsIgnoreCase(essayID)){
                essayNotFound = false;
                EssayDraft essayDraft = essay.addDraft(request.getDraftName(), request.getContents(), "", "", EssayDraft.DraftType.CONTENTS);
                essayDraft.validateEssayDraftContents(this.shortName, keywords.schoolKeywords);
            }
        }

        if(essayNotFound){
            for(SchoolInfoEssay schoolInfoEssay : schoolInfo.getEssays()) {
                if(essayID.equalsIgnoreCase(schoolInfoEssay.getEssayID())) {
                    Essay essay = new Essay(essayID, request.getEssayStatus());
                    essay.addDraft(request.getDraftName(), request.getContents(), "", "", EssayDraft.DraftType.CONTENTS);
                    essays.add(essay);
                    essayNotFound = false;
                }
            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

    }

    public void addEssayDraftUpload(String fileName, String uploadID, Essay essay, Keywords keywords, File file) throws Exception {
        EssayDraft essayDraft = essay.addDraft(fileName, "", uploadID, "", EssayDraft.DraftType.UPLOAD);
        essayDraft.validateEssayDraftUpload(shortName, keywords.schoolKeywords, file);

    }

    public JSONObject getEssay(String essayID, String essayPrompt) throws Exception{
        Essay essayRequested = null;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayRequested = essay;
            }
        }

        if(essayRequested == null){
            essayRequested = new Essay();
            essayRequested.setEssayID(essayID);
            essayRequested.setStatus(Essay.EssayStatus.NOT_STARTED);
        }

        JSONObject essayJSON = essayRequested.toJSON();
        essayJSON.put("prompt", essayPrompt);
        return essayJSON;

    }

    public Essay getEssayForDraftUpload(String essayID, SchoolInfo schoolInfo) throws Exception{

        Essay essayRequested = getEssay(essayID);
        if(essayRequested!=null){
            return essayRequested;
        }
        boolean essayNotFound = true;
        if(essayRequested == null) {
            for(SchoolInfoEssay schoolInfoEssay : schoolInfo.getEssays()) {
                if(essayID.equalsIgnoreCase(schoolInfoEssay.getEssayID())) {
                    Essay essay = new Essay(essayID, Essay.EssayStatus.IN_PROGRESS);
                    essays.add(essay);
                    essayNotFound = false;
                    return essay;
                }
            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

        return essayRequested;
    }


    public Essay getEssay(String essayID){
        Essay essayRequested = null;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                essayRequested = essay;
                return essayRequested;
            }
        }

        return essayRequested;
    }

    public EssayDraft getEssayDraft(String essayID, String draftID) throws Exception{

        Essay essay = getEssay(essayID);
        if(essay==null){
            throw new Exception("Did not find essay with ID "+essayID);
        }
        for(EssayDraft essayDraft : essay.getDrafts()){
            if(draftID.equalsIgnoreCase(essayDraft.getId())){
                return essayDraft;
            }
        }

        throw new Exception("Did not find draft with ID " + draftID);
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




    public void updateEssayDraft(EssayDraftRequest request, String essayID, String draftID, HashMap<String, List<String>> schoolKeywords) throws Exception {
        boolean essayNotFound = true;
        for (Essay essay : essays) {
            if (essay.getEssayID().equalsIgnoreCase(essayID)) {
                if(request.getContents()!=null) {
                    EssayDraft essayDraft = essay.updateDraftContents(draftID, request.getContents(), request.getUrl());
                    essayNotFound = false;
                    essayDraft.validateEssayDraftContents(shortName, schoolKeywords);
                }
            }
        }

        if(essayNotFound){
            throw new Exception("Did not find an essay with the essayID "+essayID);
        }

    }

    public void updateNote(NotesRequest notesRequest, String noteID) throws Exception {

        boolean noteNotFound = true;

        for(Note note : this.notes) {
            if(note.getNoteID().equalsIgnoreCase(noteID)) {
                note.setNoteContents(notesRequest.getContents());
                if(note.getTitle()!=null) {
                    note.setTitle(notesRequest.getTitle());
                }
                else{
                    note.setTitle("");
                }
                note.setTitle(notesRequest.getTitle());
                noteNotFound = false;
            }
        }

        if(noteNotFound) {
            throw new Exception("Did not find an essay with the noteID "+noteID);
        }

    }

    public void deleteNote(String noteID) throws Exception {

        Note deleteNote = null;

        for(Note note : this.notes) {
            if(note.getNoteID().equalsIgnoreCase(noteID)) {
                deleteNote = note;
            }
        }

        if(deleteNote == null) {
            throw new Exception("Did not find an essay with the noteID "+noteID);
        }

        this.notes.remove(deleteNote);

    }

    public void updateRecommender(RecommenderRequest recommenderRequest, String recommenderID) throws Exception {

        boolean recommendationFound = false;

        for(Recommendation recommendation : this.recommendations){
            if(recommendation.getId().equalsIgnoreCase(recommenderID)){
                recommendation.setContents(recommenderRequest.getContents());
                recommendationFound = true;
            }
        }

        if(!recommendationFound) {
            throw new Exception("Did not find recommendation ");
        }

    }


    public Recommendation getRecommender(String recommenderID) throws Exception{

        for(Recommendation recommendation : this.recommendations) {
            if(recommendation.getId().equalsIgnoreCase(recommenderID)){
                return recommendation;
            }
        }

        throw new Exception("Did not find recommendation");
    }

    public Note getNote(String noteID) throws Exception {

        Note foundNote = null;

        for(Note note : this.notes) {
            if(note.getNoteID().equalsIgnoreCase(noteID)) {
                foundNote = note;
            }
        }

        if(foundNote == null) {
            throw new Exception("Did not find an essay with the noteID "+noteID);
        }

        return foundNote;

    }



    public void addNote(NotesRequest notesRequest) throws Exception {

        Note note = new Note(notesRequest.getContents(), notesRequest.getTitle());
        this.getNotes().add(note);

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

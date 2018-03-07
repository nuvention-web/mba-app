package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Essay {

    private String essayID;
    private EssayStatus status;

    public List<EssayDraft> getDrafts() {
        return drafts;
    }

    private List<EssayDraft> drafts = new ArrayList<>();

    public enum EssayStatus{
        NOT_STARTED,
        IN_PROGRESS,
        DRAFT1_IN_PROGRESS, DRAFT1_COMPLETE,
        DRAFT2_IN_PROGRESS, DRAFT2_COMPLETE,
        DRAFT3_IN_PROGRESS, DRAFT3_COMPLETE,
        DRAFT4_IN_PROGRESS, DRAFT4_COMPLETE,
        DRAFT5_IN_PROGRESS, DRAFT5_COMPLETE,
        REVIEW_IN_PROGRESS,
        COMPLETE,
        NOT_NEEDED,
    }

    public Essay(){

    }

    public Essay(String id, EssayStatus status){
        this.essayID = id;
        this.status = status;
    }

    public Essay(String title){
        status = EssayStatus.NOT_STARTED;
    }

    public String getEssayID() {
        return essayID;
    }

    public EssayStatus getStatus() {
        return status;
    }

    public void setStatus(EssayStatus status) {
        this.status = status;
    }

    public void setEssayID(String essayID) {
        this.essayID = essayID;
    }

    public EssayDraft addDraft(String draftName, String contents, String uploadID, String url, EssayDraft.DraftType draftType){

        EssayDraft essayDraft = new EssayDraft();
        essayDraft.setContents(contents);
        essayDraft.setUrl(url);
        essayDraft.setUploadID(uploadID);
        essayDraft.setDraftType(draftType);
        draftName = draftName == null ? "Draft "+ Integer.toString(drafts.size()+1) : draftName;
        essayDraft.setDraftName(draftName);
        this.drafts.add(essayDraft);
        return essayDraft;
    }


    public void deleteDraft(String id) throws Exception{

        EssayDraft draftToDelete = null;

        for(EssayDraft draft : drafts) {
            if(draft.getId().equalsIgnoreCase(id)) {
                draftToDelete = draft;
            }
        }

        if(draftToDelete==null) {
            throw new Exception("Did not find a draft with draft id "+id);
        }

        drafts.remove(draftToDelete);

    }

    public EssayDraft updateDraftContents(String id, String contents, String url) throws Exception {
        EssayDraft draftToUpdate = null;

        for(EssayDraft draft : drafts) {
            if(draft.getId().equalsIgnoreCase(id)) {
                draftToUpdate = draft;
            }
        }

        if(draftToUpdate==null) {
            throw new Exception("Did not find a draft with essayID "+id);
        }

        if(contents!=null) {
            draftToUpdate.setContents(contents);
        }
        else if(url!=null){
            draftToUpdate.setUrl(url);
        }

        return draftToUpdate;

    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject essayJSON = new JSONObject(stringWriter.toString());
        return essayJSON;
    }


}

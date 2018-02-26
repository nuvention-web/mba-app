package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Essay {

    private String essayID;
    private EssayStatus status;
    List<EssayDraft> drafts = new ArrayList<>();

    public enum EssayStatus{
        NOT_STARTED,
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

    public void addDraft(String draftName, String contents){
        EssayDraft essayDraft = new EssayDraft();
        essayDraft.setContents(contents);
        essayDraft.setDraftName(draftName);
        this.drafts.add(essayDraft);

    }


    public void deleteDraft(String id) throws Exception{

        EssayDraft draftToDelete = null;

        for(EssayDraft draft : drafts) {
            if(draft.getId().equalsIgnoreCase(id)) {
                draftToDelete = draft;
            }
        }

        if(draftToDelete==null) {
            throw new Exception("Did not find a draft with essayID "+id);
        }

        drafts.remove(draftToDelete);

    }

    public void updateDraft(String id, String contents) throws Exception {
        EssayDraft draftToUpdate = null;

        for(EssayDraft draft : drafts) {
            if(draft.getId().equalsIgnoreCase(id)) {
                draftToUpdate = draft;
            }
        }

        if(draftToUpdate==null) {
            throw new Exception("Did not find a draft with essayID "+id);
        }

        draftToUpdate.setContents(contents);


    }

}

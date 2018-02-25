package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mbaapp.core.Essay;

/**
 * Created by jnag on 2/24/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EssayDraftRequest {

    private String draftName;
    private String contents;
    private String draftID;
    private Essay.EssayStatus essayStatus;

    public String getDraftName() {
        return draftName;
    }

    public String getContents() {
        return contents;
    }

    public String getDraftID(){
        return draftID;
    };

    public Essay.EssayStatus getEssayStatus() {
        return essayStatus;
    };
}

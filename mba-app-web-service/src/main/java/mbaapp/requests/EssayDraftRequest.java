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
    private String url;
    private Essay.EssayStatus essayStatus;
    private String id;

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getDraftName() {
        return draftName;
    }

    public String getContents() {
        return contents;
    }

    public Essay.EssayStatus getEssayStatus() {
        return essayStatus;
    };


}

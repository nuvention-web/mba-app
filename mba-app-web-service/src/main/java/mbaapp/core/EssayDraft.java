package mbaapp.core;

import java.util.UUID;

/**
 * Created by jnag on 2/24/18.
 */
public class EssayDraft {

    private String draftName;
    private String id;
    private String contents;

    public EssayDraft() {
         this.id = UUID.randomUUID().toString();

    }

    public String getDraftName() {
        return draftName;
    }

    public String getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }


    public void setDraftName(String draftName) {
        this.draftName = draftName;
    }


    public void setContents(String contents) {
        this.contents = contents;
    }
}

package mbaapp.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.UUID;

/**
 * Created by jnag on 2/28/18.
 */
public class Note {

    private String id;
    private String contents;
    private String title;

    public String getNoteID() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Note(String contents, String title){
        this.contents = contents;
        this.id = UUID.randomUUID().toString();
        this.title = title == null ? "" : title;
    }

    public void setNoteContents(String contents) {
        this.contents = contents;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject noteJSON = new JSONObject(stringWriter.toString());
        return noteJSON;
    }

}

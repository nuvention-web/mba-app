package mbaapp.core;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by jnag on 4/14/18.
 */
public class Review {

    String date;
    String id;
    String name;
    String email;
    ReviewComments reviewComments;
    String personalMessage;


    public String getDate() {
        return date;
    }

    public String getPersonalMessage() {
        return personalMessage;
    }

    public void setPersonalMessage(String personalMessage) {
        this.personalMessage = personalMessage;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getID() {
        return id;
    }

    public void setReviewComments(ReviewComments reviewComments){
        this.reviewComments = reviewComments;
    }

    public ReviewComments getReviewComments(){
        return reviewComments;
    }

    public Review(String date, String name, String email, String personalMessage) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.id = id;
        this.name = name;
        this.email = email;
        this.personalMessage = personalMessage;
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject reviewJSON = new JSONObject(stringWriter.toString());
        return reviewJSON;
    }



}

package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

/**
 * Created by jnag on 4/15/18.
 */
public class ReviewRequest {
    private String comments;

    public String getComments() {
        return comments;
    }


    public ReviewRequest(String comments) {
        this.comments = comments;
    }

    public ReviewRequest(){

    }
}

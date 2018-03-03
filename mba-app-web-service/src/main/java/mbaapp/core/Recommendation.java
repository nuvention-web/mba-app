package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.StringWriter;
import java.util.UUID;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendation {

    private String id;
    private String recommender;
    private RecommendationStatus status;
    private String contents;

    public Recommendation(String recommender) {
        this.recommender = recommender;
        this.status = RecommendationStatus.NOT_CONTACTED;
        this.id = UUID.randomUUID().toString();
    }

    enum RecommendationStatus{
        NOT_CONTACTED, CONTACTED, IN_PROGRESS, SUBMITTED
    }

    public String getRecommender() {
        return recommender;
    }

    public RecommendationStatus getStatus() {
        return status;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return this.contents;
    }

    public String getId() {
        return id;
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject recJSON = new JSONObject(stringWriter.toString());
        return recJSON;
    }
}

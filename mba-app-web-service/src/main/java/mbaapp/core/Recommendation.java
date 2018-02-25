package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recommendation {

    @Id
    private String id;
    private String recommender;
    private RecommendationStatus status;

    public Recommendation(String recommender) {
        this.recommender = recommender;
        this.status = RecommendationStatus.NOT_CONTACTED;
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
}

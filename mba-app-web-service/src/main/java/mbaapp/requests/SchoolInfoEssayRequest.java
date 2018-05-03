package mbaapp.requests;

/**
 * Created by jnag on 2/24/18.
 */
public class SchoolInfoEssayRequest {

    String essayID;
    String essayPrompt;
    String isRequired;

    public String getEssayID() {
        return essayID;
    }

    public String getEssayPrompt() {
        return essayPrompt;
    }

    public String getIsRequired() {return isRequired; }
}

package mbaapp.core;

/**
 * Created by jnag on 2/24/18.
 */
public class SchoolInfoEssay {

    private String essayID;
    private String essayPrompt;

    public String getEssayID() {
        return essayID;
    }

    public String getEssayPrompt() {
        return essayPrompt;
    }

    public void setEssayID(String essayID) {
        this.essayID = essayID;
    }

    public void setEssayPrompt(String essayPrompt) {
        this.essayPrompt = essayPrompt;
    }

    public SchoolInfoEssay(String essayID, String essayPrompt) {
        this.essayID = essayID;
        this.essayPrompt =essayPrompt;
    }
}

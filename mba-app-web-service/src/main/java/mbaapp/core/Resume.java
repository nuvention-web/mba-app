package mbaapp.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.UUID;

/**
 * Created by jnag on 5/12/18.
 */
public class Resume extends MBABase{

    private String resumeName;
    private String resumeID;
    private String uploadID;
    private String brevityScore;
    private String impactScore;
    private String gradeBlurb;
    private String depthScore;
    private String percentile;
    private String advice;
    private String analysisDone;
    private String date;


    public Resume() {
        resumeID = UUID.randomUUID().toString();
        analysisDone = "False";
        date = getCurrentTime();
    }

    public String getDate() {
        return date;
    }

    public String getBrevityScore() {
        return brevityScore;
    }

    public String getImpactScore() {
        return impactScore;
    }

    public String getAnalysisDone() {
        return analysisDone;
    }

    public void setAnalysisDone(String analysisDone) {
        this.analysisDone = analysisDone;
    }

    public String getGradeBlurb() {
        return gradeBlurb;
    }

    public String getDepthScore() {
        return depthScore;
    }

    public String getPercentile() {
        return percentile;
    }

    public String getAdvice() {
        return advice;
    }

    public void setBrevityScore(String brevityScore) {
        this.brevityScore = brevityScore;
    }

    public void setImpactScore(String impactScore) {
        this.impactScore = impactScore;
    }

    public void setGradeBlurb(String gradeBlurb) {
        this.gradeBlurb = gradeBlurb;
    }

    public void setDepthScore(String depthScore) {
        this.depthScore = depthScore;
    }

    public void setPercentile(String percentile) {
        this.percentile = percentile;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }


    public String getResumeID() {
        return resumeID;
    }

    public String getUploadID() {
        return uploadID;
    }

    public String getResumeName() {

        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject resumeJSON = new JSONObject(stringWriter.toString());
        return resumeJSON;
    }

}

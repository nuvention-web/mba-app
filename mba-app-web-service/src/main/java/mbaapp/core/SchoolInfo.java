package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 2/14/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfo {

    @Id
    public String id;

    private String name;

    private String location;

    private String shortName;

    private String round1Deadline;

    private String round2Deadline;

    private String round3Deadline;

    private String round4Deadline;

    private List<SchoolInfoEssay> essays;

    private String medianGMAT;

    private String avgGPA;

    private String avgGMAT;

    private String acceptanceRate;

    private String logoURL;

    public SchoolInfo(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getShortName() {
        return shortName;
    }

    public String getRound1Deadline() {
        return round1Deadline;
    }

    public String getRound2Deadline() {
        return round2Deadline;
    }

    public String getRound3Deadline() {
        return round3Deadline;
    }

    public String getRound4Deadline() {
        return round4Deadline;
    }

    public SchoolInfo(String name, String shortName, String location, String round1Deadline, String round2Deadline, String round3Deadline,
                      String round4Deadline){
        this.name = name;
        this.shortName = shortName;
        this.location = location;
        this.round1Deadline = round1Deadline;
        this.round2Deadline = round2Deadline;
        this.round3Deadline = round3Deadline;
        this.round4Deadline = round4Deadline;
        this.essays = new ArrayList<>();
    }

    public void addEssay(SchoolInfoEssay essay){
        this.essays.add(essay);
    }

    public List<SchoolInfoEssay> getEssays(){
        return essays;
    }

    public String getMedianGMAT() {
        return medianGMAT;
    }

    public String getAvgGPA() {
        return avgGPA;
    }

    public String getAvgGMAT() {
        return avgGMAT;
    }

    public String getAcceptanceRate() {
        return acceptanceRate;
    }

    public String getLogoURL() {
        return logoURL;
    }
}

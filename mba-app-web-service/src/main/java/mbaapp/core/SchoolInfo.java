package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

/**
 * Created by jnag on 2/14/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfo {

    @Id
    public String id;

    private String name;

    private String location;

    private String round1Deadline;

    private String round2Deadline;

    private String round3Deadline;

    private String round4Deadline;

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

    public SchoolInfo(String name, String location, String round1Deadline, String round2Deadline, String round3Deadline,
                      String round4Deadline){
        this.name = name;
        this.location = location;
        this.round1Deadline = round1Deadline;
        this.round2Deadline = round2Deadline;
        this.round3Deadline = round3Deadline;
        this.round4Deadline = round4Deadline;
    }
}

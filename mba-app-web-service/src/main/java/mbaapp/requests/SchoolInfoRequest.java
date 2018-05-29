package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoolInfoRequest {

    private String name;

    private String schoolName;

    private String address;

    private String shortName;

    private String round1Deadline;

    private String round2Deadline;

    private String round3Deadline;

    private String round4Deadline;

    private List<String> keywords;

    private String avgGPA;

    private String avgGMAT;

    private String experience;

    private String logoURL;

    private String applicationSite;

    private String contactInfo;

    private String info;

    private String pictureURL;

    private List<SchoolInfoEssayRequest> essays;

    private String enrollment;

    private String phone;

    private String email;

    private String tuition;

    private String ranking;

    public String getRanking() {
        return ranking;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getTuition() {
        return tuition;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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

    public List<SchoolInfoEssayRequest> getEssays() { return essays;}

    public String getSchoolName() {
        return schoolName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getAvgGPA() {
        return avgGPA;
    }

    public String getAvgGMAT() {
        return avgGMAT;
    }

    public String getExperience() {
        return experience;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getApplicationSite() {
        return applicationSite;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getInfo() {
        return info;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}

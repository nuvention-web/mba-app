package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import mbaapp.requests.SchoolInfoEssayRequest;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
    private String round0Deadline;
    private String round1Deadline;
    private String round2Deadline;
    private String round3Deadline;
    private String round4Deadline;
    private List<SchoolInfoEssay> essays;
    private List<String> keywords;
    private String avgGPA;
    private String avgGMAT;
    private String experience;
    private String logoURL;
    private String applicationSite;
    private String info;
    private String pictureURL;
    private String enrollment;
    private String phone;
    private String email;
    private String tuition;
    private String ranking;

    public SchoolInfo(){

    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
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

    public String getRound0Deadline() {
        return round0Deadline;
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

    public List<String> getKeywords() {
        return keywords;
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

    public String getAvgGPA() {
        return avgGPA;
    }

    public String getAvgGMAT() {
        return avgGMAT;
    }

    public String getLogoURL() {

        return "http://portal.myapp.mba/school-logos/" + logoURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setRound0Deadline(String round0Deadline) {
        this.round0Deadline = round0Deadline;
    }

    public void setRound1Deadline(String round1Deadline) {
        this.round1Deadline = round1Deadline;
    }

    public void setRound2Deadline(String round2Deadline) {
        this.round2Deadline = round2Deadline;
    }

    public void setRound3Deadline(String round3Deadline) {
        this.round3Deadline = round3Deadline;
    }

    public void setRound4Deadline(String round4Deadline) {
        this.round4Deadline = round4Deadline;
    }

    public void setEssays(List<SchoolInfoEssay> essays) {
        this.essays = essays;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setAvgGPA(String avgGPA) {
        this.avgGPA = avgGPA;
    }

    public void setAvgGMAT(String avgGMAT) {
        this.avgGMAT = avgGMAT;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public void setApplicationSite(String applicationSite) {
        this.applicationSite = applicationSite;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTuition(String tuition) {
        this.tuition = tuition;
    }

    public String getExperience() {
        return experience;
    }

    public String getApplicationSite() {
        return applicationSite;
    }

    public String getInfo() {
        return info;
    }

    public String getPictureURL() {
        return "http://portal.myapp.mba/school-pictures/" +pictureURL;
    }

    public String getEnrollment() {
        return enrollment;
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


    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject schoolJSON = new JSONObject(stringWriter.toString());
        return schoolJSON;
    }

}

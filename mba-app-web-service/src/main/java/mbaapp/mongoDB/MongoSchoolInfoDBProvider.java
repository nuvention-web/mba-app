package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import mbaapp.core.SchoolInfoEssay;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.requests.SchoolInfoEssayRequest;
import mbaapp.requests.SchoolInfoRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jnag on 2/14/18.
 */
@Service("mongoSchoolDB")
public class MongoSchoolInfoDBProvider implements SchoolInfoDBProvider {


    @Autowired
    private SchoolInfoRepository repository;

    public List<SchoolInfo> getAllSchools() {
        return repository.findAll();
    }

    public SchoolInfo getSchool(String schoolName) {
        return repository.findByName(schoolName);
    }

    public void addSchool(SchoolInfoRequest schoolRequest) throws Exception{

        String schoolName = schoolRequest.getName();
        if(schoolName==null || schoolName.isEmpty()) {
            throw new Exception("Missing schoolName");
        }

        String shortName = schoolRequest.getShortName();
        if(shortName==null || shortName.isEmpty()) {
            throw new Exception("Missing shortName");
        }

        String location = schoolRequest.getAddress();
        if(location == null || location.isEmpty()) {
            throw new Exception("Missing location");
        }
        String round1 = schoolRequest.getRound1Deadline();
        if(round1==null || round1.isEmpty()) {
            throw new Exception("Missing round1 deadline");
        }

        String round2 = schoolRequest.getRound2Deadline() == null ? "" : schoolRequest.getRound2Deadline();
        String round3 = schoolRequest.getRound3Deadline() == null ? "" : schoolRequest.getRound3Deadline();
        String round4 = schoolRequest.getRound4Deadline() == null ? "" : schoolRequest.getRound4Deadline();

        SchoolInfo school = new SchoolInfo(schoolName, shortName, location, round1, round2, round3, round4);
        for(SchoolInfoEssayRequest essayRequest : schoolRequest.getEssays()) {
            SchoolInfoEssay schoolInfoEssay = new SchoolInfoEssay(essayRequest.getEssayID(), essayRequest.getEssayPrompt(), essayRequest.getIsRequired());
            school.addEssay(schoolInfoEssay);

        }

        school.setEmail(schoolRequest.getEmail());
        school.setApplicationSite(schoolRequest.getApplicationSite());
        school.setAvgGMAT(schoolRequest.getAvgGMAT());
        school.setName(schoolRequest.getName());
        school.setShortName(schoolRequest.getShortName());
        school.setAvgGPA(schoolRequest.getAvgGPA());
        school.setEmail(schoolRequest.getEmail());
        school.setApplicationSite(schoolRequest.getApplicationSite());
        school.setEnrollment(schoolRequest.getEnrollment());
        school.setExperience(schoolRequest.getExperience());
        school.setInfo(schoolRequest.getInfo());
        school.setKeywords(schoolRequest.getKeywords());
        school.setLocation(schoolRequest.getAddress());
        school.setLogoURL(schoolRequest.getLogoURL());
        school.setPhone(schoolRequest.getPhone());
        school.setPictureURL(schoolRequest.getPictureURL());
        school.setInfo(schoolRequest.getInfo());
        school.setTuition(schoolRequest.getTuition());
        school.setRanking(schoolRequest.getRanking());

        repository.save(school);
    }
}

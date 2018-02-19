package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import mbaapp.providers.SchoolInfoDBProvider;
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

    public void addSchool(SchoolInfoRequest schoolInfoRequest) throws Exception{

        String schoolName = schoolInfoRequest.getName();
        if(schoolName==null || schoolName.isEmpty()) {
            throw new Exception("Missing schoolName");
        }

        String shortName = schoolInfoRequest.getShortName();
        if(shortName==null || shortName.isEmpty()) {
            throw new Exception("Missing shortName");
        }

        String location = schoolInfoRequest.getLocation();
        if(location == null || location.isEmpty()) {
            throw new Exception("Missing location");
        }
        String round1 = schoolInfoRequest.getRound1Deadline();
        if(round1==null || round1.isEmpty()) {
            throw new Exception("Missing round1 deadline");
        }

        String round2 = schoolInfoRequest.getRound2Deadline() == null ? "" : schoolInfoRequest.getRound2Deadline();
        String round3 = schoolInfoRequest.getRound3Deadline() == null ? "" : schoolInfoRequest.getRound3Deadline();
        String round4 = schoolInfoRequest.getRound4Deadline() == null ? "" : schoolInfoRequest.getRound4Deadline();

        SchoolInfo school = new SchoolInfo(schoolName, shortName, location, round1, round2, round3, round4);
        repository.save(school);
    }
}

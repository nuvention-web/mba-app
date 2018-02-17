package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import mbaapp.providers.SchoolInfoDBProvider;
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

    public void addSchool(JSONObject payload) throws Exception{

        String schoolName = payload.optString("schoolName");
        if(schoolName.isEmpty()) {
            throw new Exception("Missing schoolName");
        }
        String location = payload.optString("location");
        if(location.isEmpty()) {
            throw new Exception("Missing location");
        }
        String round1 = payload.optString("round1");
        if(round1.isEmpty()) {
            throw new Exception("Missing round1");
        }
        String round2 = payload.optString("round2");
        String round3 = payload.optString("round3");
        String round4 = payload.optString("round4");

        SchoolInfo school = new SchoolInfo(schoolName, location, round1, round2, round3, round4);
        repository.save(school);
    }
}

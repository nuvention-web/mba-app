package mbaapp.core;

import mbaapp.Application;
import mbaapp.providers.SchoolInfoDBProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jnag on 3/1/18.
 */
@Component
@Scope(value = "singleton")
public class Keywords {

    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;

    public HashMap<String, List<String>> schoolKeywords = new HashMap<>();


    public void loadKeyWords(){
        List<SchoolInfo> schools = schoolInfoDBProvider.getAllSchools();
        for(SchoolInfo schoolInfo : schools) {
            List<String> keywords = schoolInfo.getKeywords();
            if(!schoolKeywords.containsKey(schoolInfo.getShortName())){
                schoolKeywords.put(schoolInfo.getShortName(), new ArrayList<>());
            }
            schoolKeywords.get(schoolInfo.getShortName()).addAll(schoolInfo.getKeywords());
        }
    }

    @PostConstruct
    public void init(){
        loadKeyWords();
    }


}

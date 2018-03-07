package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.Keywords;
import mbaapp.core.SchoolInfo;
import mbaapp.providers.SchoolInfoDBProvider;
import mbaapp.requests.SchoolInfoRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.*;

/**
 * Created by jnag on 2/14/18.
 */
@RestController
@RequestMapping("/mba/schools")
public class SchoolInfoEndpoint {

    Logger logger = Logger.getLogger(SchoolInfoEndpoint.class.getName());

    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;

    @PostMapping()
    @CrossOrigin
    @ApiOperation(value = "Add a new school to the DB ")
    public ResponseEntity<String> addSchool(@RequestBody SchoolInfoRequest schoolInfoRequest) {
        try {

            schoolInfoDBProvider.addSchool(schoolInfoRequest);

            return new ResponseEntity<>("Added School", HttpStatus.CREATED);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @CrossOrigin
    @ApiOperation(value = "Retrieves all the schools from the DB")
    public List<SchoolInfo> getSchools(){
        return schoolInfoDBProvider.getAllSchools();

    }

    @PostMapping("/load")
    @CrossOrigin
    @ApiOperation(value = "Retrieves all the schools from the DB")
    public void load(){
//        Keywords keywords = Keywords.getInstance();
//        keywords.loadKeyWords();
    }

}


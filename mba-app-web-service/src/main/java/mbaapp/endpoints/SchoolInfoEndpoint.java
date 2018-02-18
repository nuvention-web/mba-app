package mbaapp.endpoints;

import io.swagger.annotations.ApiOperation;
import mbaapp.core.SchoolInfo;
import mbaapp.providers.SchoolInfoDBProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "Add a new school to the DB ")
    public ResponseEntity<String> addSchool(@RequestBody String payloadString) {
        try {

            JSONObject payload = new JSONObject(payloadString);
            schoolInfoDBProvider.addSchool(payload);

            return new ResponseEntity<>("Added School", HttpStatus.CREATED);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    @ApiOperation(value = "Retrieves all the schools from the DB")
    public List<SchoolInfo> getSchools(){
        return schoolInfoDBProvider.getAllSchools();

    }
}


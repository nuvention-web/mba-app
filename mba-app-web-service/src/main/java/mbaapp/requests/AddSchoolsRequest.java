package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by jnag on 2/19/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddSchoolsRequest {

    public List<String> schools;

    public List<String> getSchools() {
        return schools;
    }
}

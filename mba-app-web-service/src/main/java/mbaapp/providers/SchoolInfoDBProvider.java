package mbaapp.providers;

import mbaapp.core.SchoolInfo;
import mbaapp.requests.SchoolInfoRequest;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jnag on 2/14/18.
 */
public interface SchoolInfoDBProvider {

    public List<SchoolInfo> getAllSchools();

    public SchoolInfo getSchool(String school);

    public void addSchool(SchoolInfoRequest schoolInfoRequest) throws Exception;

}

package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jnag on 2/14/18.
 */
public interface SchoolInfoRepository extends MongoRepository<SchoolInfo, String> {

    public SchoolInfo findByName(String name);

    public SchoolInfo findByShortName(String shortName);

}

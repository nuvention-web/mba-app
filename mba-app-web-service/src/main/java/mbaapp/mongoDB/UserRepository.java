package mbaapp.mongoDB;

import mbaapp.core.SchoolInfo;
import mbaapp.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jnag on 2/14/18.
 */
public interface UserRepository extends MongoRepository<User, String> {

    public User findByEmail(String email);

    public User findByUserID(String id);

}

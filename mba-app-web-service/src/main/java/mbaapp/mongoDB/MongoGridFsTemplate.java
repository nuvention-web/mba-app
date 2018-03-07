package mbaapp.mongoDB;

import mbaapp.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jnag on 3/2/18.
 */
public interface MongoGridFsTemplate extends MongoRepository<User, String> {
}

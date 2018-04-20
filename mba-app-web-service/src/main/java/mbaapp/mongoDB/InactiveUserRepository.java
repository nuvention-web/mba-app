package mbaapp.mongoDB;
import mbaapp.core.InactiveUser;
import mbaapp.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by jnag on 4/21/18.
 */
public interface InactiveUserRepository extends MongoRepository<InactiveUser, String> {

    public InactiveUser findByEmail(String email);
}
package mbaapp.mongoDB;

import mbaapp.core.InactiveUser;
import mbaapp.core.User;
import mbaapp.providers.InactiveUserDBProvider;
import mbaapp.requests.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by jnag on 4/21/18.
 */
@Service("inactiveUserMongoDB")
public class MongoInactiveUserDBProvider implements InactiveUserDBProvider {

    @Autowired
    private InactiveUserRepository inactiveUserRepository;


    @Override
    public InactiveUser getInactiveUser(String email) {
        return inactiveUserRepository.findByEmail(email);
    }


    @Override
    public void deleteInactiveUser(InactiveUser user) {
        inactiveUserRepository.delete(user.getId());
    }


    @Override
    public InactiveUser createUser(CreateUserRequest createUserRequest) throws Exception {

        if (createUserRequest.getName() == null || createUserRequest.getName().isEmpty()) {
            throw new Exception("The name is missing");
        }

        if (createUserRequest.getEmail() == null || createUserRequest.getEmail().isEmpty()) {
            throw new Exception("The email is missing");
        }

        if (createUserRequest.getPassword() == null || createUserRequest.getPassword().length==0) {
            throw new Exception("The password is missing");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(java.nio.CharBuffer.wrap(createUserRequest.getPassword()));

        InactiveUser inactiveUser = new InactiveUser(createUserRequest.getName(), createUserRequest.getEmail(), hashedPassword);

        return inactiveUser;
    }


    @Override
    public void saveUser(InactiveUser inactiveUser) throws Exception {

        inactiveUserRepository.save(inactiveUser);

    }


}

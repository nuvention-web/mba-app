package mbaapp.providers;

import mbaapp.core.InactiveUser;
import mbaapp.requests.CreateUserRequest;

/**
 * Created by jnag on 4/21/18.
 */
public interface InactiveUserDBProvider {

    public InactiveUser getInactiveUser(String email);

    public void deleteInactiveUser(InactiveUser user);

    public InactiveUser createUser(CreateUserRequest createUserRequest) throws Exception;

    public void saveUser(InactiveUser inactiveUser) throws Exception;

}

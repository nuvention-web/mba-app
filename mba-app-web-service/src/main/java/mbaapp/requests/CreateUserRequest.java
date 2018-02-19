package mbaapp.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jnag on 2/19/18.
 */
@JsonIgnoreProperties
public class CreateUserRequest {

    String name;
    String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

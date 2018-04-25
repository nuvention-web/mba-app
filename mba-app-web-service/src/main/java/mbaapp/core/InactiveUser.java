package mbaapp.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Random;

/**
 * Created by jnag on 4/21/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InactiveUser {

    @Id
    public String id;
    public String name;
    public String email;
    public String password;
    public String code;
    Date date;

    public String getId() {
        return id;
    }

    public InactiveUser() {

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public InactiveUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        Random random = new Random();
        int max = 99999;
        int min = 10000;
        int codeInt = random.nextInt((max - min) + 1) + min;
        code = Integer.toString(codeInt);
        date = new Date();

    }

    public String getPassword() {
        return password;
    }
}

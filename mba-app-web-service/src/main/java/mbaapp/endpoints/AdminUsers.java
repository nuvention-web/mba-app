package mbaapp.endpoints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jnag on 5/27/18.
 */
public class AdminUsers {

    public static List<String> adminUsers = new ArrayList<>();

    public static List<String> getAdminUsers(){

        adminUsers.add("jyotishman22@gmail.com");

        return adminUsers;
    }
}

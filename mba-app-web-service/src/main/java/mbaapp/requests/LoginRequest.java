package mbaapp.requests;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

/**
 * Created by jnag on 4/19/18.
 */
public class LoginRequest {

    String email;
    char[] password;

    public String getEmail() {
        return email;
    }

    public char[] getPassword() { return password; }

}

package mbaapp.requests;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

/**
 * Created by jnag on 4/21/18.
 */
public class ActivateUserRequest {

    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public ActivateUserRequest() {
    }
}

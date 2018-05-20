package mbaapp.requests;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

/**
 * Created by jnag on 5/19/18.
 */
public class ChangePasswordRequest {

    private String newPassword;
    private String currentPassword;

    public ChangePasswordRequest() {
    }


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }
}

package mbaapp.requests;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

/**
 * Created by jnag on 5/19/18.
 */

public class ScoreRequest {

    private String greScore;
    private String gmatScore;
    private String targetGreScore;
    private String targetGmatScore;
    private String gpa;

    public ScoreRequest() {
    }

    public String getGreScore() {
        return greScore;
    }

    public String getGmatScore() {
        return gmatScore;
    }

    public String getTargetGreScore() {
        return targetGreScore;
    }

    public String getTargetGmatScore() {
        return targetGmatScore;
    }

    public String getGpa() {
        return gpa;
    }
}

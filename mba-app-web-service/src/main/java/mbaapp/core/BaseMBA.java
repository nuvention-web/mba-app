package mbaapp.core;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jnag on 5/6/18.
 */
public class BaseMBA {

    @Ignore
    protected String getCurrentTime(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

}

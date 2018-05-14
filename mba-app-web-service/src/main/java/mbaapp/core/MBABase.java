package mbaapp.core;/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jnag on 5/14/18.
 */
public class MBABase {

    protected String getCurrentTime(){
        TimeZone tz = TimeZone.getDefault();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return dateFormat.format(date);
    }

}

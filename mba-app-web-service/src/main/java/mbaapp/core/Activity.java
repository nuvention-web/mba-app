package mbaapp.core;

import java.util.Date;

/**
 * Created by jnag on 2/19/18.
 */
public class Activity {

    private Date timeStamp;
    private String message;

    public Activity() {

    }

    public Activity (String message) {
        timeStamp = new Date();
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }
}

package mbaapp.core;

import java.util.Date;

/**
 * Created by jnag on 6/5/18.
 */
public class ProfilePDF extends BaseMBA{

    private String uuid;
    private String date;

    public ProfilePDF() {
        date = getCurrentTime();
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDate() {
        return date;
    }
}

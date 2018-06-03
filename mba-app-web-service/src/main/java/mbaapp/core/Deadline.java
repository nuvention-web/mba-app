package mbaapp.core;

import java.util.Date;

/**
 * Created by jnag on 6/3/18.
 */
public class Deadline {

    private Date deadline;
    private String description;

    public Deadline() {
    }

    public Deadline(Date deadline, String description) {
        this.deadline = deadline;
        this.description = description;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }
}

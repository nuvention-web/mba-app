package mbaapp.core;

import java.util.UUID;

/**
 * Created by jnag on 6/3/18.
 */
public class Task {

    private String name;
    private String details;
    private String id;
    private String date;


    public Task() {

    }

    public Task(String name, String details, String date) {
        this.name = name;
        this.details = details;
        this.date = date;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

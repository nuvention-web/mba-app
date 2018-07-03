package mbaapp.core;

import java.util.UUID;

/**
 * Created by jnag on 6/3/18.
 */
public class Task implements Comparable<Task>{

    private String name;
    private String details;
    private String id;
    private String date;
    private boolean completed;


    public Task() {

    }

    @Override
    public int compareTo(Task o) {
        return date.compareTo(o.getDate());
    }


    public Task(String name, String details, String date) {
        this.name = name;
        this.details = details;
        this.date = date;
        this.id = UUID.randomUUID().toString();
        completed = false;
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}

package mbaapp.requests;

/**
 * Created by jnag on 6/3/18.
 */
public class AddTaskRequest {

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddTaskRequest() {
    }
}

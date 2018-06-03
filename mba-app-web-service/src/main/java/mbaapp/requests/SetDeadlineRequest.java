package mbaapp.requests;

/**
 * Created by jnag on 6/3/18.
 */
public class SetDeadlineRequest {

    private String deadline;

    public SetDeadlineRequest() {
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }
}

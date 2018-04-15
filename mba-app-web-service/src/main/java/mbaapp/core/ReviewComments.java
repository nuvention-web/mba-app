package mbaapp.core;

/**
 * Created by jnag on 4/14/18.
 */
public class ReviewComments {
    String date;
    String comment;
    String uploadID;
    String fileName;

    public ReviewComments(String date, String comment) {
        this.date = date;
        this.comment = comment;
    }

    public ReviewComments(String date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ReviewComments() {

    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {

        return date;
    }

    public void setUploadID(String id) {
        this.uploadID = id;
    }

    public String getUploadID() {
        return uploadID;
    }

    public String getComment() {
        return comment;
    }
}

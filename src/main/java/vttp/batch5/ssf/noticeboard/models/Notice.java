package vttp.batch5.ssf.noticeboard.models;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class Notice {

    @NotNull(message = "Required")
    @NotEmpty(message = "Required")
    @Size(min = 3, max = 128)
    private String title;

    @NotNull(message = "Required")
    @NotEmpty(message = "Required")
    @Email(message = "Invalid email format")
    private String poster;

    @NotNull(message = "Required")
    @Future(message = "Date can only be set after today")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postDate;

    @NotNull(message = "Choose at least 1 category")
    @NotEmpty(message = "Choose at least 1 category")
    private List<String> categories;

    @NotNull(message = "Cannot leave blank")
    @NotEmpty(message = "Cannot leave blank")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Notice(String title, String poster, Date postDate, List<String> categories, String text) {
        this.title = title;
        this.poster = poster;
        this.postDate = postDate;
        this.categories = categories;
        this.text = text;
    }

    public Notice() {}
}

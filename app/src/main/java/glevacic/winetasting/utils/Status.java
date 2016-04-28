package glevacic.winetasting.utils;

public class Status {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status(String title, String description) {
        this.title = title;
        this.description = description;
    }
}

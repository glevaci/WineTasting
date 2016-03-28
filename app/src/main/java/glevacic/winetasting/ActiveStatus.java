package glevacic.winetasting;

public class ActiveStatus {
    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ActiveStatus(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return title + ": " + description;
    }
}

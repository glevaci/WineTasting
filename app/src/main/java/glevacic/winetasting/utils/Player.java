package glevacic.winetasting.utils;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<ActiveStatus> activeStatuses;

    public Player(String name) {
        this.name = name;
        activeStatuses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ActiveStatus> getActiveStatuses() {
        return activeStatuses;
    }

    public void addActiveStatus(ActiveStatus status) {
        activeStatuses.add(status);
    }

    // TODO remove active status by id
}

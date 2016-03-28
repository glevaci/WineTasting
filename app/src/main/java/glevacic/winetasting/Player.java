package glevacic.winetasting;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private static String name;
    private List<ActiveStatus> activeStatuses;

    public Player(String n) {
        name = n;
        activeStatuses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ActiveStatus> getActiveStatuses() {
        return activeStatuses;
    }

}

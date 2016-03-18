package glevacic.winetasting;

import java.util.List;

public class Player {

    private static String name;
    private List<ActiveStatus> activeStatuses;

    private String getName() {
        return name;
    }

    private List<ActiveStatus> getActiveStatuses() {
        return activeStatuses;
    }

}

package glevacic.winetasting.utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class Player implements ParentListItem {

    private String name;

    private List<ActiveStatus> activeStatuses;

    public Player(String name) {
        this.name = name;
        activeStatuses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addActiveStatus(ActiveStatus status) {
        activeStatuses.add(status);
    }

    @Override
    public List<?> getChildItemList() {
        return activeStatuses;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    // TODO remove active status by id
}

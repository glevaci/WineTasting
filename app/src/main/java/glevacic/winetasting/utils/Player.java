package glevacic.winetasting.utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class Player implements ParentListItem {

    private String name;

    private List<Status> statuses;

    public Player(String name) {
        this.name = name;
        statuses = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStatus(Status status) {
        statuses.add(status);
    }

    public void removeAllStatuses() {
        statuses.clear();
    }

    @Override
    public List<?> getChildItemList() {
        return statuses;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    public int getNumberOfStatuses() {
        return statuses.size();
    }

}

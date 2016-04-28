package glevacic.winetasting.utils;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

public class PlayerListItem implements ParentListItem {

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public PlayerListItem(Player pl) {
        player = pl;
    }

    @Override
    public List<?> getChildItemList() {
        return player.getChildItemList();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
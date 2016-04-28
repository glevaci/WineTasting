package glevacic.winetasting.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO name?
public class PlayerList {

    private List<Player> players;
    private int currentPlayerIndex;

    public PlayerList() {
        players = new ArrayList<>();
        currentPlayerIndex = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentIndex() {
        return currentPlayerIndex;
    }

    public Player getNextPlayer() {
        if (players.isEmpty())
            return null;

        if (currentPlayerIndex < players.size()-1)
            ++currentPlayerIndex;
        else
            currentPlayerIndex = 0;

        return players.get(currentPlayerIndex);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}

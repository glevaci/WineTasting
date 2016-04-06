package glevacic.winetasting.utils;

import java.util.ArrayList;
import java.util.List;

// TODO name?
public class PlayerList {

    private ArrayList<Player> players;
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

    public Player getNextPlayer() {
        if (players.isEmpty())
            return null;

        if (currentPlayerIndex < players.size()-1)
            ++currentPlayerIndex;
        else
            currentPlayerIndex = 0;

        return players.get(currentPlayerIndex);
    }

    public void removePlayer(String playerName) {
        for (Player player : players)
            if (player.getName().equals(playerName))
                players.remove(player);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}

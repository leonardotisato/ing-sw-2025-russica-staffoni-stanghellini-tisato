package it.polimi.ingsw.cg04.client.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import it.polimi.ingsw.cg04.model.Game;

import java.util.ArrayList;
import java.util.List;


public class ClientModel {
    private Game game = null;
    private String nickname = null;
    private final List<String> logs = new ArrayList<>();
    private PropertyChangeListener listener;

    /**
     * Retrieves the game instance associated with this ClientModel.
     *
     * @return the current game instance, or null if no game is set.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Retrieves the nickname used by the client for the game
     *
     * @return the nickname as a String, or null if no nickname is set.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the current game for the client model.
     * This method also notifies the view of a game update event.
     *
     * @param game the game instance to be set.
     * @param nickname the nickname of the player associated with the game.
     */
    public void setGame(Game game, String nickname) {

        if (game == null) System.out.println("################################## Game is null!!!!!!!!!! ##################################");
        this.game = game;
        this.nickname = nickname;

        this.listener.propertyChange(new PropertyChangeEvent(
                this,
                "GAME_UPDATE",
                nickname,
                this.game
                ));
    }

    /**
     * Retrieves the list of logs maintained by the client model.
     *
     * @return a list of strings representing the logs.
     */
    public List<String> getLogs() {
        return logs;
    }

    /**
     * Adds a list of log messages to the client's log history. Maintains a maximum
     * of 10 log entries by removing the oldest log when the limit is exceeded.
     * Notifies listeners of log updates.
     *
     * @param newLogs a list of new log messages to be added
     */
    public void addLog(List<String> newLogs) {
        for (String l : newLogs) {
            logs.add(l);
            if (logs.size() > 10) {
                logs.removeFirst();
            }
        }

        this.listener.propertyChange(new PropertyChangeEvent(
                this,
                "LOGS_UPDATE",
                null,
                this.logs));
    }

    /**
     * Notifies listeners with a list of joinable games available.
     * The method triggers a property change event named "JOINABLE_GAMES",
     * passing the provided list of game information.
     *
     * @param infos a list of {@link Game.GameInfo} objects containing
     *              details of joinable games.
     */
    public void displayJoinableGames(List<Game.GameInfo> infos){
        this.listener.propertyChange(new PropertyChangeEvent(
                this,
                "JOINABLE_GAMES",
                null,
                infos
        ));
    }

    /**
     * Sets the listener that will handle property change events for this object.
     *
     * @param listener the {@link PropertyChangeListener} to be notified of property changes.
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }
}

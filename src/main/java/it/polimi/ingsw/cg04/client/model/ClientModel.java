package it.polimi.ingsw.cg04.client.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import it.polimi.ingsw.cg04.model.Game;

import java.util.ArrayList;
import java.util.List;


public class ClientModel {
    private Game game = null;
    private final List<String> logs = new ArrayList<>();
    private PropertyChangeListener listener;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game, String nickname) {

        if (game == null) System.out.println("################################## Game is null!!!!!!!!!! ##################################");
        this.game = game;

        this.listener.propertyChange(new PropertyChangeEvent(
                this,
                "GAME_UPDATE",
                nickname,
                this.game
                ));
    }

    public List<String> getLogs() {
        return logs;
    }

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

    public void displayJoinableGames(List<Game.GameInfo> infos){
        this.listener.propertyChange(new PropertyChangeEvent(
                this,
                "JOINABLE_GAMES",
                null,
                infos
        ));
    }

    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }
}

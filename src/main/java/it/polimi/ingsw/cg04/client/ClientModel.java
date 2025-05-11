package it.polimi.ingsw.cg04.client;

import it.polimi.ingsw.cg04.client.view.Observer;
import it.polimi.ingsw.cg04.model.Game;

import java.util.ArrayList;
import java.util.List;


public class ClientModel implements Observable {

    private Game game = null;
    private final List<String> logs = new ArrayList<>();
    private Observer observer;


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        notifyObserversGameUpdate();
    }

    public List<String> getLogs() {
        return logs;
    }

    public void addLog(String log) {
        logs.add(log);
        notifyObserversLogsUpdate();
    }

    @Override
    public void addObserver(Observer observer) {
        observer  = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        observer = null;
    }

    @Override
    public void notifyObserversGameUpdate() {
        observer.updateGame();
    }

    @Override
    public void notifyObserversLogsUpdate() {
        observer.updateLogs();
    }
}

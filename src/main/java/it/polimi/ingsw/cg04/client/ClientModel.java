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

    public void setGame(Game game, String nickname) {
        this.game = game;
        notifyObserversGameUpdate(nickname);
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
        notifyObserversLogsUpdate();
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer  = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observer = null;
    }

    @Override
    public void notifyObserversGameUpdate(String nickname) {
        observer.updateGame(nickname);
    }

    @Override
    public void notifyObserversLogsUpdate() {
        observer.updateLogs();
    }
}

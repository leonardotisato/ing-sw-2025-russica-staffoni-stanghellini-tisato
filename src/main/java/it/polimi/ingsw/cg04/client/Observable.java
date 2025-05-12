package it.polimi.ingsw.cg04.client;


import it.polimi.ingsw.cg04.client.view.Observer;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserversGameUpdate(String nickname);
    void notifyObserversLogsUpdate();
}

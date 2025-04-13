package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;

public interface InitAction extends PlayerAction {

    void execute(GamesController controller);

    boolean checkAction(GamesController controller);
}

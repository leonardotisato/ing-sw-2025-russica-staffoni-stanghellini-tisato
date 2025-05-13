package it.polimi.ingsw.cg04.model.PlayerActions.LobbyActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.exceptions.InvalidActionException;

public class CreateGameAction extends InitAction {

    private final int gameLevel;
    private final int maxPlayers;
    private final PlayerColor playerColor;

    public CreateGameAction(int gameLevel, int maxPlayers, String playerName, PlayerColor playerColor) {
        super(playerName);
        this.gameLevel = gameLevel;
        this.maxPlayers = maxPlayers;
        this.playerColor = playerColor;
    }


    @Override
    public void execute(GamesController controller) {
        Game g = new Game(gameLevel, maxPlayers, controller.getGames().size(), nickname, playerColor);
        controller.addGame(g);
    }

    @Override
    public boolean checkAction(GamesController controller) throws InvalidActionException {

        if (gameLevel != 1 && gameLevel != 2) {
            throw new InvalidActionException("Invalid game level");
        }

        if (maxPlayers < 2 || maxPlayers > 4) {
            throw new InvalidActionException("Invalid number of players");
        }

        if (nickname == null || nickname.isEmpty()) {
            throw new InvalidActionException("Invalid nickname");
        }

        return true;
    }

    @Override
    public void execute(Player player) {
        throw new RuntimeException("Player does not exist");
    }

    @Override
    public boolean checkAction(Player player) {
        throw new RuntimeException("Player does not exist");
    }

}

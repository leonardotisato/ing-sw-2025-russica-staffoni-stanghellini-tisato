package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.controller.GamesController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;

public class CreateGameAction implements InitAction {


    private final int gameLevel;
    private final int maxPlayers;
    private final String playerName;
    private final PlayerColor playerColor;

    public CreateGameAction(int gameLevel, int maxPlayers, String playerName, PlayerColor playerColor) {
        this.gameLevel = gameLevel;
        this.maxPlayers = maxPlayers;
        this.playerName = playerName;
        this.playerColor = playerColor;
    }


    @Override
    public void execute(GamesController controller) {
        Game g = new Game(gameLevel, maxPlayers, controller.getGames().size(), playerName, playerColor);
        controller.addGame(g);
    }

    @Override
    public boolean checkAction(GamesController controller) {

        if (gameLevel != 1 && gameLevel != 2) {
            return false;
        }

        if (maxPlayers < 2 || maxPlayers > 4) {
            return false;
        }

        if (playerName == null || playerName.isEmpty() || controller.isNickNameTaken(playerName)) {
            return false;
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

    @Override
    public String getPlayerNickname() {
        return playerName;
    }


}

package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.tiles.HousingTile;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class HandleCrewAction implements PlayerAction{
    List<Integer> numCrewMembersLost;
    List<Coordinates> coordinates;
    Game game;

    public HandleCrewAction(List<Coordinates> coordinates, List<Integer> numCrewMembersLost, Game context) {
        this.coordinates = new ArrayList<>(coordinates);
        this.numCrewMembersLost = new ArrayList<>(numCrewMembersLost);
        this.game = context;
    }

    public void execute(Player player) {
        AdventureCardState gameState = (AdventureCardState)game.getGameState();
        if (numCrewMembersLost.isEmpty()) {
            gameState.getPlayed().set(gameState.getCurrPlayerIdx(), 1);
            gameState.setCurrPlayerIdx(gameState.getCurrPlayerIdx() + 1);
            return;
        }
        else {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                if (coordinates.get(i).isIn(player.getShip().getTilesMap().get("HousingTile"))) {
                    player.getShip().removeCrew(CrewType.HUMAN, coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
                }
                else throw new RuntimeException("you can't remove crew members here, not an HousingTile!");
            }
            gameState.getPlayed().replaceAll(ignored -> 1);
            player.updateCredits(game.getCurrentAdventureCard().getEarnedCredits());
            player.move(-game.getCurrentAdventureCard().getDaysLost());
            return;
        }
    }
}

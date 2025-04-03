package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.ExPlayerState;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandleBoxesAction implements PlayerAction{
    private Game game;
    private final List<Coordinates> coordinates;
    private final List<Map<BoxType,Integer>> boxes;
    public HandleBoxesAction(List<Coordinates> coordinates, List<Map<BoxType,Integer>> boxes, Game game) {
        this.coordinates = coordinates;
        this.boxes = boxes;
        this.game = game;
    }

    public void execute(Player player) {
        AdventureCardState gameState = (AdventureCardState)game.getGameState();
        if (boxes == null) {
            gameState.getPlayed().set(gameState.getCurrPlayerIdx(), 1);
            gameState.setCurrPlayerIdx(gameState.getCurrPlayerIdx() + 1);
            return;
        }
        else {
            if (player.getShip().getNumCrew() >= game.getCurrentAdventureCard().getMembersNeeded()) {
                for (int i = 0; i < coordinates.size(); i++) {
                    player.getShip().setBoxes(boxes.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
                }
            }
            else throw new RuntimeException("not enough crew");
            gameState.getPlayed().replaceAll(ignored -> 1);
            player.move(-game.getCurrentAdventureCard().getDaysLost());
            return;
        }
    }
}

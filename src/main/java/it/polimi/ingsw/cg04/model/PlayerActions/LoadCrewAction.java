package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.GameStates.GameState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;

public class LoadCrewAction implements PlayerAction {

    private final String playerNickname;
    private final Coordinates pinkAlienCoords, brownAlienCoords;

    public LoadCrewAction(String playerNickname, Coordinates pinkAlienCoords, Coordinates brownAlienCoords) {
        this.playerNickname = playerNickname;
        this.pinkAlienCoords = pinkAlienCoords;
        this.brownAlienCoords = brownAlienCoords;
    }

    @Override
    public void execute(Player player) {
        GameState state = player.getGame().getGameState();
        state.loadCrew(player, pinkAlienCoords, brownAlienCoords);
    }


    @Override
    public boolean checkAction(Player player) {

        List<Coordinates> housingTileList = player.getShip().getTilesMap().get("HousingTile");

        // if both not null and the same coords case
        if (pinkAlienCoords != null && brownAlienCoords != null && pinkAlienCoords.equals(brownAlienCoords)) {
            return false;
        }

        // pink alien check
        boolean pinkCheck = false;
        if (pinkAlienCoords != null) {
            if (pinkAlienCoords.isIn(housingTileList) && player.getShip().getTile(pinkAlienCoords.getX(), pinkAlienCoords.getY()).getSupportedCrewType().contains(CrewType.PINK_ALIEN)) {
                pinkCheck = true;
            }
        } else {
            pinkCheck = true;
        }

        // brown alien check
        boolean brownCheck = false;
        if (brownAlienCoords != null) {
            if (brownAlienCoords.isIn(housingTileList) && player.getShip().getTile(brownAlienCoords.getX(), brownAlienCoords.getY()).getSupportedCrewType().contains(CrewType.BROWN_ALIEN)) {
                brownCheck = true;
            }
        } else {
            brownCheck = true;
        }

        return brownCheck && pinkCheck;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}

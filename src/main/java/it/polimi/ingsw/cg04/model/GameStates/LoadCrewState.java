package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates.AdventureCardState;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.BuildPlayerState;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

public class LoadCrewState extends AdventureCardState {

    public LoadCrewState(Game game) {
        super(game);
    }

    public void loadCrew(Player player, Coordinates pinkAlienCoords, Coordinates brownAlienCoords) throws InvalidStateException {
        if (sortedPlayers.get(currPlayerIdx).equals(player)) {

            Ship ship = player.getShip();

            // fill selected coordinates with alien
            if (pinkAlienCoords != null) {
                ship.removeCrew(CrewType.HUMAN, pinkAlienCoords.getX(), pinkAlienCoords.getY(), 2);
                ship.addCrew(CrewType.PINK_ALIEN, pinkAlienCoords.getX(), pinkAlienCoords.getY());
            }
            if (brownAlienCoords != null) {
                ship.removeCrew(CrewType.HUMAN, brownAlienCoords.getX(), brownAlienCoords.getY(), 2);
                ship.addCrew(CrewType.BROWN_ALIEN, brownAlienCoords.getX(), brownAlienCoords.getY());
            }

            currPlayerIdx++;

            // when all player loaded their ships the flight can begin
            if (currPlayerIdx == sortedPlayers.size()) {
                triggerNextState();
            }

        } else {
            throw new InvalidStateException("Load crew not allowed for player: " + player.getName());
        }
    }

        public String render (String nickname){
            StringBuilder stringBuilder = new StringBuilder("\n");
            stringBuilder.append("State: Load crew").append("\n").append("\n");
            for (Player p : context.getPlayers()) {
                stringBuilder.append("player: ").append(p.getName()).append("\n");
                stringBuilder.append("Color: ").append(p.getColor()).append("\n");
                stringBuilder.append("Position: ").append(p.getRanking()).append("\n").append("\n");
            }
            stringBuilder.append("Your ship:").append("\n").append("\n");
            stringBuilder.append(context.getPlayer(nickname).getShip().draw()).append("\n").append("\n");
            stringBuilder.append("It's time to load crew!");
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
    }


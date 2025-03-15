package it.polimi.ingsw.cg04.model.adventureCards;

import it.polimi.ingsw.cg04.model.Game;

public class Epidemic extends AdventureCard {

    public Epidemic() {
        super();
    }

    public void solveEffect(Game game) {

        /*
        *
        * for each player
        *   player.getShip
        *   bool at_least_one_occupied = false
        *   for each tile
        *       if tile.type() == HousingTile && tile.occupied() == true
        *           list near_tiles = ship.near_tiles()
        *           for each tile1 in near_tiles
        *               if tile1.type == HousingTile && tile1.occupied
        *                   tile1.remove_crew       // update also ship parametres
        *                   at_least_one_occupied = true
        *           if(at_least_one_occupied == true)
        *               tile.remove_crew
        *               at_least_one_occupied = false
        *
        */

    }
}

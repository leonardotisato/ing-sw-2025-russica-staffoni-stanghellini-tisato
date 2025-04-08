package it.polimi.ingsw.cg04.model.PlayerActions;

import it.polimi.ingsw.cg04.model.Player;

// todo: test me
public class ChooseBatteryAction implements PlayerAction {

    private final String playerNickname;
    private final int x, y;

    public ChooseBatteryAction(String playerNick, int x, int y) {
        this.playerNickname = playerNick;
        this.x = x;
        this.y = y;
    }

    public void execute(Player player) {
        if (x >= 0 && y >= 0) {
            player.getShip().removeBatteries(1, x, y);
        }
    }

    public boolean checkAction(Player player) {

        // x=y=-1 means "Dont use batteries"
        if (x == -1 && y == -1) {
            return true;
        }

        // if received coords are out of bound
        if (!player.getShip().isSlotValid(x, y)) {
            return false;
        }

        // check if input coordinates match a battery tile with batteries on it
        Integer batteriesInSelectedCoords = player.getShip().getTile(x, y).getNumBatteries();

        if (batteriesInSelectedCoords == null) {
            System.out.println("No batteries in selected coords");
            return false;
        }

        if (batteriesInSelectedCoords <= 0) {
            System.out.println("Expected batteries to be greater than 0");
            return false;
        }

        return true;
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }

    @Override
    public String getType() {
        return "CHOOSE_BATTERY";
    }
}

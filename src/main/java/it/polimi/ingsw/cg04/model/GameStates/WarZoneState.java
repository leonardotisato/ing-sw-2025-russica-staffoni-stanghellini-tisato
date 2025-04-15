package it.polimi.ingsw.cg04.model.GameStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.PlayerActions.PlayerAction;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.adventureCards.AdventureCard;
import it.polimi.ingsw.cg04.model.adventureCards.WarZone;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.cg04.model.enumerations.CrewType.BROWN_ALIEN;
import static it.polimi.ingsw.cg04.model.enumerations.CrewType.PINK_ALIEN;

public class WarZoneState extends AdventureCardState {
    private final List<Double> firePower;
    private List<Integer> propulsionPower;
    private List<Integer> crewSize;
    private Integer penaltyIdx;

    private boolean rolled = false;
    private int currShotIdx;
    private int dice;
    private Direction direction;
    private Attack attack;

    private int worstPlayerState;
    private Integer worstPlayerIdx;

    private final int F_INIT = 0;
    private final int F_PROVIDE_BATTERY = 1;
    private final int F_CORRECT_SHIP = 2;
    private final int F_DONE = 3;

    private final int INIT = 0;
    private final int DONE = 1;
    private final int WORST = 2;
    private final int WILL_FIGHT = 3;

    private final int leftBoundary, rightBoundary;
    private final int upBoundary, downBoundary;


    public WarZoneState(Game game) {
        super(game);
        firePower = new ArrayList<Double>();
        propulsionPower = new ArrayList<Integer>();
        crewSize = new ArrayList<Integer>();
        penaltyIdx = 0;
        currPlayerIdx = 0;
        worstPlayerState = F_INIT;
        worstPlayerIdx = 0;
        currShotIdx = -1;

        upBoundary = 5;
        downBoundary = 9;

        if (game.getLevel() == 2) {
            leftBoundary = 4;
            rightBoundary = 10;
        } else {
            leftBoundary = 5;
            rightBoundary = 9;
        }
    }

    private boolean isAllDone(List<Integer> played) {
        for (Integer integer : played) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }

    private void triggerNextRound() {
        if(played.get(worstPlayerIdx) == WILL_FIGHT && worstPlayerState == F_DONE) {
            rolled = false;
            currShotIdx++;

            if (currShotIdx >= card.getAttacks().size()) {
                played.set(worstPlayerIdx, DONE);
                triggerNextPenalty();
            } else {
                worstPlayerState = F_INIT;
                System.out.println("Colpo in arrivo!");
            }
        }
    }

    private void triggerNextPenalty() {
        if(isAllDone(played)) {
            penaltyIdx++;
            worstPlayerIdx = 0;
            currPlayerIdx = 0;
            if(penaltyIdx >= 3) {
                triggerNextState();
            }
            else {
                played.replaceAll(ignored -> INIT);
                System.out.println("Nuovo round di WarZone! Round numero: " + (penaltyIdx + 1));
            }
        }
    }

    @Override
    public void countCrewMembers(Player player) {
        if(card.getParameterCheck().get(penaltyIdx).equals("CREW") && player.getName().equals(sortedPlayers.getFirst().getName())) {

            for(int i = 0; i < sortedPlayers.size(); i++) {
                crewSize.add(sortedPlayers.get(i).getShip().getNumCrew());
                System.out.println("Player " + sortedPlayers.get(i).getName() + " ha " + crewSize.get(i) + " membri dell'equipaggio.");
                played.set(i, DONE);
            }
            for(int i = 1; i < crewSize.size(); i++) {
                if(crewSize.get(i) < crewSize.get(worstPlayerIdx)) {
                    worstPlayerIdx = i;
                }
            }
            System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + "ha meno membri dell'equipaggio di tutti.");
            played.set(worstPlayerIdx, WORST);

            if(card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                System.out.println("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                played.set(worstPlayerIdx, DONE);

                sortedPlayers = context.getSortedPlayers();
                triggerNextPenalty();
            }
            else if(card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                played.set(worstPlayerIdx, WILL_FIGHT);
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
        }
        else {
            System.out.println("It's not " + player.getName() + " turn.");
        }
    }

    @Override
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) {
        Ship ship = player.getShip();

        if (card.getParameterCheck().get(penaltyIdx).equals("CANNONS") &&  player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            double fire = ship.getBaseFirePower();

            if(fire > 0) {
                fire += 2 * player.getShip().getNumCrewByType(PINK_ALIEN);
            }

            // activate double cannons if played provided them
            if (batteries != null && doubleCannons != null) {
                // remove batteries used
                for (Coordinates c : batteries) {
                    ship.removeBatteries(1, c.getX(), c.getY());
                }

                // increase firePower adding new double cannons based on orientation
                for (Coordinates c : doubleCannons) {
                    // if laser is up bonus is 2
                    if (ship.getTile(c.getX(), c.getY()).getConnection(Direction.UP) == Connection.GUN) {
                        fire += 2;
                    } else {
                        //else bonus is 1
                        fire += 1;
                    }
                }
            }
            firePower.add(fire);
            played.set(currPlayerIdx, DONE);
            System.out.println("Player " + player.getName() + " ha " + firePower.get(currPlayerIdx) + " di potenza di fuoco.");
            currPlayerIdx++;
            if(isAllDone(played)) {
                for(int i = 1; i < firePower.size(); i++) {
                    if (firePower.get(i) < firePower.get(worstPlayerIdx)) {
                        worstPlayerIdx = i;
                    }
                }
                System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza di fuoco di tutti.");
                played.set(worstPlayerIdx, WORST);
                if(card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                    System.out.println("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                    sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                    played.set(worstPlayerIdx, DONE);

                    sortedPlayers = context.getSortedPlayers();
                    triggerNextPenalty();
                }
                else if(card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                    System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                    played.set(worstPlayerIdx, WILL_FIGHT);
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            }
        }
    }


    @Override
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) {
        if (played.get(worstPlayerIdx).equals(WORST) &&  player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            System.out.println("Player " + player.getName() + " ha rimosso i membri dall'equipaggio.");
            played.set(worstPlayerIdx, DONE);
            triggerNextPenalty();
        }
        else {
            System.out.println("It's not " + player.getName() + " turn.");
        }
    }


    @Override
    public void usePropulsors(Player player, List<Coordinates> coordinates, List<Integer> usedBatteries) {
        if (card.getParameterCheck().get(penaltyIdx).equals("PROPULSORS") && player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            int deltaPropPower = 0;
            for(int i = 0; i < usedBatteries.size(); i++) {
                player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
                deltaPropPower += usedBatteries.get(i);
            }
            if(player.getShip().getBasePropulsionPower() > 0) {
                deltaPropPower += 2 * player.getShip().getNumCrewByType(BROWN_ALIEN);
            }
            propulsionPower.add(player.getShip().getBasePropulsionPower() + deltaPropPower * 2);
            played.set(currPlayerIdx, DONE);
            System.out.println("Player " + player.getName() + " ha " + propulsionPower.get(currPlayerIdx) + " di potenza dei propulsori.");
            currPlayerIdx++;
            if(isAllDone(played)) {
                for(int i = 1; i < propulsionPower.size(); i++) {
                    if (propulsionPower.get(i) < propulsionPower.get(worstPlayerIdx)) {
                        worstPlayerIdx = i;
                    }
                }
                System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza dei propulsori di tutti.");
                played.set(worstPlayerIdx, WORST);
                if(card.getPenaltyType().get(penaltyIdx).equals("LOSEBOX")) {
                    player.getShip().removeBestBoxes(card.getLostGoods());
                    played.set(worstPlayerIdx, DONE);
                    System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getLostGoods() + " risorse.");
                    triggerNextPenalty();
                } else if (card.getPenaltyType().get(penaltyIdx).equals("LOSECREW")) {
                    System.out.println("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " deve rimuovere " + card.getLostMembers() + " membri dell'equipaggio.");
                }
            }
        }
        else {
            System.out.println("It's not " + player.getName() + " turn.");
        }
    }

    @Override
    public void chooseBattery(Player player, int x, int y) {
        if (rolled && worstPlayerState == F_PROVIDE_BATTERY && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    worstPlayerState = F_CORRECT_SHIP;
                }
                else {
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            } else { // player used battery and he is done for the round
                player.getShip().removeBatteries(1, x, y);
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
        }
        else {
            System.out.println("Wrong action or player!.");
        }
    }

    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) {
        // check if player actually needs to fix his ship
        if (rolled && worstPlayerState == F_CORRECT_SHIP && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {
            for (Coordinates coordinates : coordinatesList) {
                player.getShip().breakTile(coordinates.getX(), coordinates.getY());
            }
            // if fixes make the ship legal player is done for this round
            if (player.getShip().isShipLegal()) {
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
        }
        else {
            System.out.println("Wrong action or player!");
        }
    }

    @Override
    public void rollDice(Player player) {

        if (!rolled && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName()) && worstPlayerState == F_INIT) {
            dice = context.getBoard().rollDices();
            rolled = true;


            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currShotIdx);
            attack = context.getCurrentAdventureCard().getAttack(currShotIdx);

            System.out.println("Attack type is: " + attack + " meteor, direction is: " + direction + " dice result is: " + dice);

            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    System.out.println("Ship wasn't hit.");
                    triggerNextRound();
                    return;
                } else {
                    dice -= upBoundary - 1;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    System.out.println("Ship wasn't hit");
                    triggerNextRound();
                    return;
                } else {
                    dice -= leftBoundary - 1;
                }
            }

            // create list that maps player with what he needs to do

            int hitState = player.getShip().checkHit(direction, attack, dice, "shot");

            // if player is safe set its state to done for this attack
            if (hitState == -1) {
                System.out.println("Player: " + player.getName() + " was not hit");
                worstPlayerState = F_INIT;
                triggerNextRound();
            }
            // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
            else if (hitState == 2) {
                System.out.println("Player " + player.getName() + " was hit");
                player.getShip().handleHit(direction, dice);

                // if ship is legal, player is done for this attack
                if (player.getShip().isShipLegal()) {
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                } else {
                    // player needs to correct his ship
                    System.out.println("Player " + player.getName() + " deve correggere la nave.");
                    worstPlayerState = F_CORRECT_SHIP;
                }
            }
            // player can decide to use batteries to defend his ship
            else if (hitState == 0) {
                System.out.println("Player: " + player.getName() + " can use a battery to save his ship!");
                worstPlayerState = F_PROVIDE_BATTERY;
            }
        }
        else {
            System.out.println("Wrong action or player!");
        }
    }
}

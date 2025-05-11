package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Attack;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
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
                this.addLog("Colpo in arrivo!");
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
                this.addLog("Nuovo round di WarZone! Round numero: " + (penaltyIdx + 1));
            }
        }
    }

    @Override
    public void countCrewMembers(Player player) throws InvalidStateException {
        if (card.getParameterCheck().get(penaltyIdx).equals("CREW") && player.getName().equals(sortedPlayers.getFirst().getName())) {
            if(player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                for (int i = 0; i < sortedPlayers.size(); i++) {
                    crewSize.add(sortedPlayers.get(i).getShip().getNumCrew());
                    this.addLog("Player " + sortedPlayers.get(i).getName() + " ha " + crewSize.get(i) + " membri dell'equipaggio.");
                    played.set(i, DONE);
                }
                for (int i = 1; i < crewSize.size(); i++) {
                    if (crewSize.get(i) < crewSize.get(worstPlayerIdx)) {
                        worstPlayerIdx = i;
                    }
                }
                this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + "ha meno membri dell'equipaggio di tutti.");
                played.set(worstPlayerIdx, WORST);

                if (card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                    this.addLog("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                    sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                    played.set(worstPlayerIdx, DONE);

                    sortedPlayers = context.getSortedPlayers();
                    triggerNextPenalty();
                } else if (card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                    this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                    played.set(worstPlayerIdx, WILL_FIGHT);
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            }
        } else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    @Override
    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) throws InvalidStateException {
        Ship ship = player.getShip();

        if (card.getParameterCheck().get(penaltyIdx).equals("CANNONS") &&  player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            if(player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                double fire = ship.getBaseFirePower();

                if (fire > 0) {
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
                this.addLog("Player " + player.getName() + " ha " + firePower.get(currPlayerIdx) + " di potenza di fuoco.");
                currPlayerIdx++;
                if (isAllDone(played)) {
                    for (int i = 1; i < firePower.size(); i++) {
                        if (firePower.get(i) < firePower.get(worstPlayerIdx)) {
                            worstPlayerIdx = i;
                        }
                    }
                    this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza di fuoco di tutti.");
                    played.set(worstPlayerIdx, WORST);
                    if (card.getPenaltyType().get(penaltyIdx).equals("GOBACK")) {
                        this.addLog("Player: " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getDaysLost() + " giorni di volo.");
                        sortedPlayers.get(worstPlayerIdx).move(-card.getDaysLost());
                        played.set(worstPlayerIdx, DONE);

                        sortedPlayers = context.getSortedPlayers();
                        triggerNextPenalty();
                    } else if (card.getPenaltyType().get(penaltyIdx).equals("HANDLESHOTS")) {
                        this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " subirà delle cannonate. Preparati a combattere!");
                        played.set(worstPlayerIdx, WILL_FIGHT);
                        worstPlayerState = F_DONE;
                        triggerNextRound();
                    }
                }
            }
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }


    @Override
    public void usePropulsors(Player player, List<Coordinates> coordinates, List<Integer> usedBatteries) throws InvalidStateException {
        if (card.getParameterCheck().get(penaltyIdx).equals("PROPULSORS") && player.getName().equals(sortedPlayers.get(currPlayerIdx).getName())) {
            if(player.getGame().getSortedPlayers().size() == 1) {
                this.addLog("Only 1 player is currently in the game. WarZone will be skipped.");
                triggerNextState();
            } else {
                int deltaPropPower = 0;
                for (int i = 0; i < usedBatteries.size(); i++) {
                    player.getShip().removeBatteries(usedBatteries.get(i), coordinates.get(i).getX(), coordinates.get(i).getY());
                    deltaPropPower += usedBatteries.get(i);
                }
                if (player.getShip().getBasePropulsionPower() > 0) {
                    deltaPropPower += 2 * player.getShip().getNumCrewByType(BROWN_ALIEN);
                }
                propulsionPower.add(player.getShip().getBasePropulsionPower() + deltaPropPower * 2);
                played.set(currPlayerIdx, DONE);
                this.addLog("Player " + player.getName() + " ha " + propulsionPower.get(currPlayerIdx) + " di potenza dei propulsori.");
                currPlayerIdx++;
                if (isAllDone(played)) {
                    for (int i = 1; i < propulsionPower.size(); i++) {
                        if (propulsionPower.get(i) < propulsionPower.get(worstPlayerIdx)) {
                            worstPlayerIdx = i;
                        }
                    }
                    this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " ha meno potenza dei propulsori di tutti.");
                    played.set(worstPlayerIdx, WORST);
                    if (card.getPenaltyType().get(penaltyIdx).equals("LOSEBOX")) {
                        player.getShip().removeBestBoxes(card.getLostGoods());
                        played.set(worstPlayerIdx, DONE);
                        this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " perde " + card.getLostGoods() + " risorse.");
                        triggerNextPenalty();
                    } else if (card.getPenaltyType().get(penaltyIdx).equals("LOSECREW")) {
                        this.addLog("Player " + sortedPlayers.get(worstPlayerIdx).getName() + " deve rimuovere " + card.getLostMembers() + " membri dell'equipaggio.");
                    }
                }
            }
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }


    @Override
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        if (played.get(worstPlayerIdx).equals(WORST) &&  player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {
            for (int i = 0; i < numCrewMembersLost.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            this.addLog("Player " + player.getName() + " ha rimosso i membri dall'equipaggio.");
            played.set(worstPlayerIdx, DONE);
            triggerNextPenalty();
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }


    @Override
    public void chooseBattery(Player player, int x, int y) throws InvalidStateException {
        if (rolled && worstPlayerState == F_PROVIDE_BATTERY && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName())) {

            // handle case where player decide to take the hit
            if (x == -1 && y == -1) {
                player.getShip().handleHit(direction, dice);
                if (!player.getShip().isShipLegal()) {
                    this.addLog("Player " + player.getName() + " has been hit and he should fix his ship!");
                    worstPlayerState = F_CORRECT_SHIP;
                }
                else {
                    this.addLog("Player " + player.getName() + " has been hit but his ship is still legal!");
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                }
            } else { // player used battery and he is done for the round
                this.addLog("Player " + player.getName() + " used a battery and neutralized the attack!");
                player.getShip().removeBatteries(1, x, y);
                worstPlayerState = F_DONE;
                triggerNextRound();
            }
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    @Override
    public void fixShip(Player player, List<Coordinates> coordinatesList) throws InvalidStateException {
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
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    @Override
    public void rollDice(Player player) throws InvalidStateException {

        if (!rolled && player.getName().equals(sortedPlayers.get(worstPlayerIdx).getName()) && worstPlayerState == F_INIT) {
            dice = context.getBoard().rollDices();
            rolled = true;


            // check if meteor misses completely
            direction = context.getCurrentAdventureCard().getDirection(currShotIdx);
            attack = context.getCurrentAdventureCard().getAttack(currShotIdx);


            // check whether the meteor will hit the ships
            if (direction == Direction.UP || direction == Direction.DOWN) {
                if (dice < leftBoundary || dice > rightBoundary) {
                    this.addLog("Ship wasn't hit.");
                    triggerNextRound();
                    return;
                } else {
                    dice -= upBoundary - 1;
                }
            }
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                if (dice < upBoundary || dice > downBoundary) {
                    this.addLog("Ship wasn't hit");
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
                this.addLog("Player: " + player.getName() + " was not hit");
                worstPlayerState = F_INIT;
                triggerNextRound();
            }
            // deliver guaranteed hit and check if ship is still legal if not put in correction state "2"
            else if (hitState == 2) {
                this.addLog("Player " + player.getName() + " was hit");
                player.getShip().handleHit(direction, dice);

                // if ship is legal, player is done for this attack
                if (player.getShip().isShipLegal()) {
                    worstPlayerState = F_DONE;
                    triggerNextRound();
                } else {
                    // player needs to correct his ship
                    this.addLog("Player " + player.getName() + " deve correggere la nave.");
                    worstPlayerState = F_CORRECT_SHIP;
                }
            }
            // player can decide to use batteries to defend his ship
            else if (hitState == 0) {
                this.addLog("Player: " + player.getName() + " can use a battery to save his ship!");
                worstPlayerState = F_PROVIDE_BATTERY;
            }
        }
        else {
            throw new InvalidStateException("Non è il turno di " + player.getName() + " o l'azione che ha compiuto non è valida in questo stato.");
        }
    }

    public boolean anyWorstPlayer() {
        for (Integer integer : played) {
            if (integer == WORST || integer == WILL_FIGHT) {
                return true;
            }
        }
        return false;
    }

    public String render(String playerName) {
        //count crew solo leader chiama per tutti;
        //remove crew quando sei worst
        StringBuilder stringBuilder = new StringBuilder(super.render(playerName));
        stringBuilder.append("\n".repeat(3));
        Player p = context.getPlayer(playerName);
        int playerIdx = p.getRanking() - 1;
        String challenge = card.getParameterCheck().get(penaltyIdx);
        if (!anyWorstPlayer()) {
            switch (challenge) {
                case "CANNONS":
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("It's time to compare fire powers!").append("\n");
                        stringBuilder.append("It's your turn! Send batteries to increase your fire power.").append("\n");
                    }
                    else{
                        stringBuilder.append("It's time to compare fire powers!").append("\n");
                        stringBuilder.append("Wait for your turn to send batteries to increase your fire power.").append("\n");
                    }
                    break;
                case "PROPULSORS":
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("It's time to compare propulsion powers!").append("\n");
                        stringBuilder.append("It's your turn! Send batteries to increase your propulsion power.").append("\n");
                    }
                    else{
                        stringBuilder.append("It's time to compare propulsion powers!").append("\n");
                        stringBuilder.append("Wait for your turn to send batteries to increase your propulsion power.").append("\n");
                    }
                    break;
                case "CREW":
                    if (currPlayerIdx == playerIdx) {
                        stringBuilder.append("It's time to compare the number of crew members!").append("\n");
                        stringBuilder.append("You're the leader, start the challenge!").append("\n");
                    }
                    else{
                        stringBuilder.append("It's time to compare the number of crew members!").append("\n");
                        stringBuilder.append("Wait for the leader to start the challenge").append("\n");
                    }
            }
        }
        else{
            if (card.getPenaltyType().get(currPlayerIdx).equals("LOSECREW")){
                if (played.get(playerIdx) == WORST) {
                    stringBuilder.append("You're the worst player for this challenge! Remove " + card.getLostMembers() + " crew members.").append("\n");
                }
                else{
                    stringBuilder.append("You survived this challenge!").append(penaltyIdx == card.getPenaltyType().size() ? " You're done for this warzone! Wait for the next adventure." :
                            " You're done for this challenge! Wait for the next one.").append("\n");
                }
            } else if (card.getPenaltyType().get(currPlayerIdx).equals("HANDLESHOTS")) {
                if (played.get(playerIdx) == WILL_FIGHT) {
                    switch (worstPlayerState) {
                        case F_INIT:
                            stringBuilder.append("You're the worst player for this challenge! Shot " + currShotIdx + " is coming, roll the dice to discover its direction").append("\n");
                            break;
                        case F_PROVIDE_BATTERY:
                            stringBuilder.append("You can defend your ship! Send a bettery to destroy the shot").append("\n");
                            break;
                        case F_CORRECT_SHIP:
                            stringBuilder.append("You've been hit! Fix your ship by removing tiles until it becomes legal.").append("\n");
                            break;
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}

package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SlaversState extends AdventureCardState {


    private boolean isOpponentDead = false;

    private int opponentFirePower;
    private final int reward;
    private final int delta;
    private final int crewLost;

    // states the player can be in: WAITING TO BE ATTACKED, ACTIVATE_CANNONS, REMOVE_CREW, DECIDE_REWARD, DONE
    private final int WAIT = 0;
    private final int ACTIVATE_CANNONS = 1;
    private final int REMOVE_CREW = 2;
    private final int DECIDE_REWARD = 3;
    private final int DONE = 4;

    private final List<Integer> playerStates = new ArrayList<>();

    public SlaversState(Game game) {
        super(game);

        this.opponentFirePower = card.getFirePower();
        this.reward = card.getEarnedCredits();
        this.delta = card.getDaysLost();
        this.crewLost = card.getLostMembers();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            playerStates.add(WAIT);
        }

        // first leader must compare his firepower with opponents
        playerStates.set(0, ACTIVATE_CANNONS);
    }


    public void getReward(Player player, boolean acceptReward) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == DECIDE_REWARD &&
                isOpponentDead &&
                IntStream.range(0, playerStates.size())
                        .filter(i -> i != playerIdx)
                        .allMatch(i -> playerStates.get(i) == DONE)) {
            if (acceptReward) {
                // give reward to winner
                player.updateCredits(reward);
                player.move(-delta);
                playerStates.set(playerIdx, DONE);
            } else {
                // player won but declined reward
                playerStates.set(playerIdx, DONE);
            }

            // check if every one is done
            if (isAllDone(playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Get reward not allowed for player: " + player.getName());
        }

    }

    public void compareFirePower(Player player, List<Coordinates> batteries, List<Coordinates> doubleCannons) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);
        Ship ship = player.getShip();

        if (playerStates.get(playerIdx) == ACTIVATE_CANNONS) {
            double firePower = ship.getBaseFirePower();

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
                        firePower += 2;
                    } else {
                        //else bonus is 1
                        firePower += 1;
                    }
                }
            }

            // now check if player defeated the opponent

            // player has lost
            if (firePower < opponentFirePower) {
                playerStates.set(playerIdx, REMOVE_CREW);

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // tie, player does not get reward and simply end his turn
            if (firePower == opponentFirePower) {
                playerStates.set(playerIdx, DONE);

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            if (firePower > opponentFirePower) {

                if (!isOpponentDead) {
                    isOpponentDead = true;
                    playerStates.set(playerIdx, DECIDE_REWARD);
                } else {
                    playerStates.set(playerIdx, DONE);
                }

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // check if every one is done
            if (isAllDone(playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Compare fire power not allowed for player: " + player.getName());
        }
    }

    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == REMOVE_CREW) {

            // if invalid input do nothing
            if (coordinates == null && numCrewMembersLost == null) {
                System.out.println("Remove crew is mandatory!");
                return;
            }

            // check that total sacrificed is <= to crewLost
            assert numCrewMembersLost != null;
            int totalCrewMembers = numCrewMembersLost.stream().mapToInt(Integer::intValue).sum();
            if (totalCrewMembers < crewLost) {
                System.out.println("Abort handleCrew: " + player.getName() + " did not provide enough members");
                return;
            }

            // remove crew members provided
            for (int i = 0; i < coordinates.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }

            // player lost crew and he is done for the adventure
            playerStates.set(playerIdx, DONE);

            // check if every one is done
            if (isAllDone(playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Remove crew not allowed for player: " + player.getName());
        }
    }

    // for testing purposes only!
    public void FORCE_OPPONENT_FIREPOWER(int val) {
        this.opponentFirePower = val;
    }

    private boolean isAllDone(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }
}

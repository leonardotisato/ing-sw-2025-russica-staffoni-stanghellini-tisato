package it.polimi.ingsw.cg04.model.GameStates.AdventureCardStates;

import it.polimi.ingsw.cg04.client.view.View;
import it.polimi.ingsw.cg04.client.view.gui.controllers.ViewController;
import it.polimi.ingsw.cg04.model.Game;
import it.polimi.ingsw.cg04.model.Player;
import it.polimi.ingsw.cg04.model.Ship;
import it.polimi.ingsw.cg04.model.enumerations.Connection;
import it.polimi.ingsw.cg04.model.enumerations.CrewType;
import it.polimi.ingsw.cg04.model.enumerations.Direction;
import it.polimi.ingsw.cg04.model.exceptions.InvalidStateException;
import it.polimi.ingsw.cg04.model.tiles.Tile;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.io.IOException;
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

    /**
     * Retrieves the current player states.
     *
     * @return a list of integers representing the states of the players.
     */
    public List<Integer> getPlayerStates() {
        return playerStates;
    }

    /**
     * Allows the specified player to decide whether to accept or decline a reward
     * after defeating an opponent, provided the game state allows this action.
     * If the player accepts the reward, their in-game credits and position are
     * updated.
     *
     * @param player       the player who is deciding whether to accept the reward
     * @param acceptReward true if the player accepts the reward, false if they decline
     * @throws InvalidStateException if the player is not in a valid state to receive the reward,
     *                               or if the action is not allowed
     */
    @Override
    public void getReward(Player player, boolean acceptReward) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == DECIDE_REWARD &&
                isOpponentDead &&
                IntStream.range(0, playerStates.size())
                        .filter(i -> i != playerIdx)
                        .allMatch(i -> playerStates.get(i) == DONE)) {
            if (acceptReward) {
                // give reward to the winner
                player.updateCredits(reward);
                player.move(-delta);
                playerStates.set(playerIdx, DONE);
                addLog("Player " + player.getName() + " won against the slavers, earned " + reward + " credits and sacrificed" + delta + " days of flight.");
            } else {
                // player won but declined reward
                playerStates.set(playerIdx, DONE);
                addLog("Player " + player.getName() + " won against the slavers, but declined the reward.");
            }

            // check if everyone is done
            if (isAllDone(playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Get reward not allowed for player: " + player.getName());
        }

    }

    /**
     * Compares the firepower of the given player with the opponent's firepower and updates
     * the game state accordingly based on the comparison result.
     *
     * @param player        the player whose firepower is being compared
     * @param batteries     a list of battery coordinates to be used by the player; can be null
     * @param doubleCannons a list of double cannon coordinates to be used by the player; can be null
     * @throws InvalidStateException if the action is not allowed for the given player in their current state
     */
    @Override
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

            // apply conditional alien bonus
            if (firePower > 0 && ship.getNumCrewByType(CrewType.PINK_ALIEN) == 1) {
                firePower += 2;
                this.addLog("Player " + player.getName() + " has a pink alien and gets bonus firepower!");
            }

            // now check if the player defeated the opponent

            // player has lost
            if (firePower < opponentFirePower) {

                this.addLog("Player " + player.getName() + " lost against the slavers!");
                playerStates.set(playerIdx, REMOVE_CREW);

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    this.appendLog("Now it's " + sortedPlayers.get(playerIdx + 1).getName() + "'s turn to activate his cannons!");
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            // tie, the player does not get reward and simply end his turn
            if (firePower == opponentFirePower) {
                playerStates.set(playerIdx, DONE);
                this.addLog("Player " + player.getName() + " drew against the slavers!");

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    this.appendLog("Now it's " + sortedPlayers.get(playerIdx + 1).getName() + "'s turn to activate his cannons!");
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                }
            }

            if (firePower > opponentFirePower) {

                if (!isOpponentDead) {
                    isOpponentDead = true;
                    playerStates.set(playerIdx, DECIDE_REWARD);
                    this.addLog("Player " + player.getName() + " has defeated the slavers!");
                } else {
                    this.addLog("Player " + player.getName() + " has defeated the slavers! However he is not entitled to the reward!");
                    playerStates.set(playerIdx, DONE);
                }

                // now playerIdx+1 needs to activate his cannons
                if (playerIdx + 1 < playerStates.size()) {
                    playerStates.set(playerIdx + 1, ACTIVATE_CANNONS);
                    this.appendLog("Now it's " + sortedPlayers.get(playerIdx + 1).getName() + "'s turn to activate his cannons!");
                }
            }

            // check if everyone is done
            if (isAllDone(playerStates)) {
                // transition to next adventure card
                triggerNextState();
            }
        } else {
            throw new InvalidStateException("Compare fire power not allowed for player: " + player.getName());
        }
    }


    /**
     * Removes a specified number of crew members from a player's ship during the "Remove Crew" game state.
     * Depending on the player's actions, this method either removes the crew as indicated or throws an
     * exception if the required conditions are not met. The game state is updated after the operation.
     *
     * @param player             the player who is removing crew members
     * @param coordinates        a list of coordinates specifying the tiles from which to remove crew members
     * @param numCrewMembersLost a list of integers indicating the number of crew members to be removed from each corresponding tile
     * @throws InvalidStateException if the player is not in a valid state to perform this action, or if the number of crew members provided is insufficient
     */
    @Override
    public void removeCrew(Player player, List<Coordinates> coordinates, List<Integer> numCrewMembersLost) throws InvalidStateException {
        int playerIdx = sortedPlayers.indexOf(player);

        if (playerStates.get(playerIdx) == REMOVE_CREW) {

            // handle null input
            if (coordinates == null && numCrewMembersLost == null) {
                // if player crew is less than required
                if (player.getShip().getNumCrew() <= crewLost) {

                    // remove all crew from the player's ship
                    List<Coordinates> housingTiles = player.getShip().getTilesMap().get("HousingTile");

                    for (Coordinates c : housingTiles) {
                        Tile currTile = player.getShip().getTile(c.getX(), c.getY());
                        player.getShip().removeCrew(currTile.getHostedCrewType(), c.getX(), c.getY(), currTile.getNumCrew());
                    }

                    this.addLog("Player " + player.getName() + "'s ship has no crew members...");
                    playerStates.set(playerIdx, DONE); // this player will be soon eliminated on the next flight state

                } else {
                    throw new InvalidStateException("You must remove crew members from your ship");
                }

                // check if everyone is done
                if (isAllDone(playerStates)) {
                    // transition to next adventure card
                    triggerNextState();
                }

                return;
            }

            // check that the total sacrificed is == to crewLost
            int totalCrewMembers = numCrewMembersLost.stream().mapToInt(Integer::intValue).sum();
            if (totalCrewMembers != crewLost) {
                System.out.println("Abort handleCrew: " + player.getName() + " did not provide correct amount of members");
                throw new InvalidStateException("You must remove " + crewLost + " crew members from your ship, but provided " + totalCrewMembers + " instead. If you don't have enough crew members, simply resend the removeCrew command without providing coordinates or numbers of crew members to remove.");
            }

            // remove crew members provided
            for (int i = 0; i < coordinates.size(); i++) {
                Tile currTile = player.getShip().getTile(coordinates.get(i).getX(), coordinates.get(i).getY());
                player.getShip().removeCrew(currTile.getHostedCrewType(), coordinates.get(i).getX(), coordinates.get(i).getY(), numCrewMembersLost.get(i));
            }
            this.appendLog("Player " + player.getName() + " removed the crew members.");

            // the player lost crew, and he is done for the adventure
            playerStates.set(playerIdx, DONE);

            // check if everyone is done
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

    /**
     * Checks if all elements in the provided list are equal to the DONE value.
     *
     * @param list the list of integers to check
     * @return true if all elements in the list are equal to DONE, false otherwise
     */
    private boolean isAllDone(List<Integer> list) {
        for (Integer integer : list) {
            if (integer != DONE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates the given view with the current state of the provided game object.
     *
     * @param view      the view instance that should be updated
     * @param toDisplay the game object containing the state to render on the view
     * @throws IOException if an input or output exception occurs during the view update
     */
    @Override
    public void updateView(View view, Game toDisplay) throws IOException {
        view.renderSlaversState(toDisplay);
    }

    @Override
    public void updateStateController(ViewController controller, Game game) {
        controller.updateSlaversController(game);
    }
}

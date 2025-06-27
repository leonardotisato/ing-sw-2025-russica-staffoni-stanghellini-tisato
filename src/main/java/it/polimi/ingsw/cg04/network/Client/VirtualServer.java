package it.polimi.ingsw.cg04.network.Client;

import it.polimi.ingsw.cg04.model.enumerations.BoxType;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.utils.Coordinates;

import java.util.List;
import java.util.Map;

public interface VirtualServer {
    // **** initActions ****

    /**
     * Send action to set the client's nickname serverside
     *
     * @param nickname the desired nickname to set for the virtual server
     */
    void setNickname(String nickname);

    /**
     * Send action to create a game with the specified parameters
     *
     * @param gameLevel  the level of the game to be created
     * @param maxPlayers the maximum number of players allowed in the game
     * @param color      the color chosen by the player creating the game
     */
    void createGame(int gameLevel, int maxPlayers, PlayerColor color);

    /**
     * Sends a request to join a game with the specified game ID and player color.
     *
     * @param gameId the unique identifier of the game to join
     * @param color  the color chosen by the player for the game
     */
    void joinGame(int gameId, PlayerColor color);

    // **** buildActions ****

    /**
     * Sends a request to the server to select a tile identified by its index.
     *
     * @param tileIdx the index of the tile to be chosen
     */
    void chooseTile(int tileIdx);

    /**
     * Sends a request to select a tile from the buffer, identified by its index.
     *
     * @param idx the index of the tile to be selected from the buffer
     */
    void chooseTileFromBuffer(int idx);

    /**
     * Sends a request to the server to close the currently visible face-up tiles.
     */
    void closeFaceUpTiles();

    /**
     * Sends a request to the server to draw a face-down tile.
     */
    void drawFaceDown();

    /**
     * Sends a request to the server to signal the end of the building phase at the specified position.
     *
     * @param position the position the player wants to move to after finishing the build phase
     */
    void endBuilding(int position);

    /**
     * Sends a request to the server to pick a pile identified by its index.
     *
     * @param pileIdx the index of the pile to be picked
     */
    void pickPile(int pileIdx);

    /**
     * Sends a request to the server to place a tile at the specified coordinates.
     *
     * @param x the x-coordinate where the tile should be placed
     * @param y the y-coordinate where the tile should be placed
     */
    void place(int x, int y);

    /**
     * Sends a request to the server to perform a rotation action based on the provided type.
     *
     * @param type the type of rotation to be performed
     */
    void rotate(String type);

    /**
     * Sends a request to the server to place the currently selected tile in the buffer.
     */
    void placeInBuffer();

    /**
     * Sends a request to the server to return the previously picked pile.
     */
    void returnPile();

    /**
     * Sends a request to the server to return the currently selected tile.
     */
    void returnTile();

    /**
     * Sends a request to the server to display the currently available face-up tiles.
     */
    void showFaceUp();

    /**
     * Sends a request to start a timer
     */
    void startTimer();

    /**
     * Sends a request to the server to stop the building process for all players.
     * Can only be performed when all timers have expired
     */
    void stopBuilding();

    // **** adventureCardActions ****

    /**
     * Sends a request to the server to select a battery located at the specified coordinates.
     *
     * @param x the x-coordinate of the battery to be chosen
     * @param y the y-coordinate of the battery to be chosen
     */
    void chooseBattery(int x, int y);

    /**
     * Sends a request to the server to choose a propulsor based on the specified coordinates
     * and the corresponding used batteries.
     *
     * @param coordinates a list of coordinates representing the positions of the available propulsors
     * @param usedBatteries a list of integers indicating the battery usage for each propulsor
     */
    void choosePropulsor(List<Coordinates> coordinates, List<Integer> usedBatteries);

    /**
     * Compares the crew numbers against other enemies.
     */
    void compareCrew();

    /**
     * Compares the firepower.
     *
     * @param BatteryCoords a list of coordinates representing the positions of the used batteries
     * @param CannonCoords a list of coordinates representing the positions of the activated cannons
     */
    void compareFirePower(List<Coordinates> BatteryCoords, List<Coordinates> CannonCoords);

    /**
     * Sends a command to the server to trigger the spread of an epidemic within the game's context.
     */
    void spreadEpidemic();

    /**
     * Sends a request to the server to retrieve the next adventure card.
     */
    void getNextAdventureCard();

    /**
     * Sends a request to the server to handle the player's rewards.
     *
     * @param acceptReward a boolean value indicating whether the player accepts or declines the reward
     */
    void getRewards(boolean acceptReward);

    /**
     * Handles a list of boxes associated with the provided coordinates.
     *
     * @param coords a list of coordinates indicating the positions to handle
     * @param boxes a list of mappings where each map associates a box type with its respective count
     */
    void handleBoxes(List<Coordinates> coords, List<Map<BoxType, Integer>> boxes);

    /**
     * Sends a request to the server to land on the specified planet, along with the associated coordinates
     * and boxes to handle during the landing.
     *
     * @param planetIdx the index of the target planet where the landing is to be performed
     * @param coords a list of coordinates indicating the coordinates where boxes must be placed
     * @param boxes a list of mappings where each map associates a box type with its respective count,
     *              representing the items being handled during the landing
     */
    void landToPlanet(Integer planetIdx, List<Coordinates> coords, List<Map<BoxType, Integer>> boxes);

    /**
     * Handles the removal of crew members at specified coordinates.
     *
     * @param coords a list of coordinates indicating the positions where crew members are removed
     * @param numCrewMembersLost a list of integers representing the number of crew members lost at each corresponding position
     */
    void removeCrew(List<Coordinates> coords, List<Integer> numCrewMembersLost);

    /**
     * Sends a request to the server to roll the dices.
     */
    void rollDice();

    /**
     * Sends a request to the server to trigger the "Star Dust" action, causing (some) players to move back in the flightboard
     */
    void starDust();

    /**
     * Sends a retirement request by the player
     */
    void retire();

    /**
     * Sends a request to the server to repair the ship components located at the specified coordinates.
     *
     * @param coords a list of coordinates indicating the positions on the ship that need repairs
     */
    void fixShip(List<Coordinates> coords);

    /**
     * Sends a request to the server to load crew members at the specified coordinates.
     *
     * @param pinkCoords the coordinates where the pink crew members should be loaded
     * @param brownCoords the coordinates where the brown crew members should be loaded
     */
    void loadCrew(Coordinates pinkCoords, Coordinates brownCoords);



}

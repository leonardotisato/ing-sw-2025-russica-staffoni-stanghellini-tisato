package it.polimi.ingsw.cg04.model;
import it.polimi.ingsw.cg04.model.enumerations.PlayerColor;
import it.polimi.ingsw.cg04.model.enumerations.PlayerState;
import it.polimi.ingsw.cg04.model.FlightBoard;

public class Player {

    private String name;
    private final PlayerColor color;
    private PlayerState state;
    private int loops = 0;
    private int position = 0;
    private int numCredits = 0;
    private Ship ship;
    private Game game;
    private FlightBoard flightBoard;

    public Player(String name, PlayerColor color, Game game){
        this.name = name;
        this.color = color;
        this.state = PlayerState.IN_LOBBY;
        this.game = game;
        this.flightBoard = this.game.getBoard();
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public PlayerState getState() {return state;}

    public int getLoops() {
        return loops;
    }

    public int getPosition() {
        return position;
    }

    public int getNumCredits() {
        return numCredits;
    }

    public Ship getShip() {
        return ship;
    }

    public void addCredits(int credits){
        numCredits += credits;
    }

    public void removeCredits(int credits){
        numCredits -= credits;
    }

    public void addLoop(){
        loops++;
    }

    public void setState(PlayerState state){
        this.state = state;
    }

    public Component ChooseFaceUpComponent(){
        // todo
        return null;
    }

    public Component PickFaceDownComponent(){
        // todo
        return null;
    }

    public void returnComponent(){
        // todo
    }

    public boolean bufferComponent(){
        // todo
        return false;
    }

    public boolean placeComponent(int x, int y){
        // todo
        return false;
    }


}

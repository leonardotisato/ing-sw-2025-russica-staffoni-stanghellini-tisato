package it.polimi.ingsw.cg04.model.exceptions;

public class InvalidActionException extends Exception {
    private String reason;
    public InvalidActionException(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}

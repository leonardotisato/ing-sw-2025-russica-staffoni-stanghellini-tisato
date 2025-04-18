package it.polimi.ingsw.cg04.model.exceptions;

public class InvalidStateException extends Exception{
    private final String reason;
    public InvalidStateException(String reason) {
        this.reason = reason;
    }
    public String getReason() {
        return reason;
    }
}

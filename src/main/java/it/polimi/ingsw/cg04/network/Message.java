package it.polimi.ingsw.cg04.network;

import java.io.Serializable;

public record Message(String messageType, Object payload) implements Serializable {}
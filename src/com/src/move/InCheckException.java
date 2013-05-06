package com.src.move;

import java.util.HashSet;


/**
 * Indicates that a move must be made to get out of check.
 * @author Kyle
 */
public class InCheckException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private HashSet<Move> availableMoves;

    public InCheckException(HashSet<Move> availableMoves) {
    	super("You must make a move that takes the king out of check.");
    	this.availableMoves = availableMoves;
    }
    
    public HashSet<Move> getAvailableMoves() {
    	return availableMoves;
    }
}
package com.src.move;

import com.src.grid.Location;
import com.src.pieces.Piece;


/**
 * Indicates that something has attempted to move a piece in an illegal or
 * invalid way.
 * @author Kyle
 */
public class IllegalMoveException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final Piece piece;
	private final Location location;
	
	/**
	 * Creates a new exception with a standard message stating that the given
	 * piece attempted to move to the given location, but the move was not
	 * valid.
	 * @param p The piece to move.
	 * @param loc The attempted move location.
	 */
	public IllegalMoveException(Piece p, Location loc) {
		this(p.toString() + " cannot move to location " + loc.toString() + ".", p, loc);
	}
	
	/**
	 * Creates a new exception stating that the given piece attempted to move
	 * to the given location, but the move was not valid.
	 * @param p The piece to move.
	 * @param loc The attempted move location.
	 * @param message A message describing the error.
	 */
	public IllegalMoveException(String message, Piece p, Location loc) {
		super(message);
		this.piece = p;
		this.location = loc;
	}
	
	/**
	 * @return The location the piece attempted to move to.
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * @return The piece that attempted the illegal move.
	 */
	public Piece getPiece() {
		return piece;
	}
}
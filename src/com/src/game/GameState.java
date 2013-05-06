package com.src.game;

import com.src.grid.Chessboard;
import com.src.grid.Location;
import com.src.pieces.Pawn;

/**
 * A snapshot of a frame in a game.
 * @author kyle
 */
public class GameState {
	/**
	 * True if it is white's turn; false otherwise.
	 */
	public final boolean isWhitesTurn;
	
	/**
	 * The state of the board.
	 */
	public final Chessboard board;
	
	/**
	 * The en-passant capture location, per Forsyth-Edwards Notation. If there
	 * is no location, this is null.
	 */
	public final Location ep_loc;
	
	/**
	 * The en-passant pawn, per Forsyth-Edwards Notation. This is null if there
	 * are no en-passant pawns.
	 */
	public final Pawn ep_pawn;
	
	public GameState(boolean isWhitesTurn, Chessboard board, Location ep_loc, Pawn ep_pawn) {
		super();
		this.isWhitesTurn = isWhitesTurn;
		this.board = board;
		this.ep_loc = ep_loc;
		this.ep_pawn = ep_pawn;
	}
}
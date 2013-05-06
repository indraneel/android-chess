package com.src.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import com.src.grid.Chessboard;
import com.src.grid.Location;
import com.src.move.IllegalMoveException;
import com.src.move.InCheckException;
import com.src.move.InvalidLocationException;
import com.src.move.Move;
import com.src.move.MoveSequence;
import com.src.pieces.Bishop;
import com.src.pieces.King;
import com.src.pieces.Knight;
import com.src.pieces.Pawn;
import com.src.pieces.Piece;
import com.src.pieces.Queen;
import com.src.pieces.Rook;

/**
 * Manages the user interface and the back-end.
 * @author Kyle
 */
public class Game {
	// Pieces and grid information
	private ArrayList<Piece> pieces;
	private Chessboard grid;
	private HashSet<Location> white_locs, black_locs;
	
	// Move and turn information
	private boolean isRecording;
	private MoveSequence moves;
	private boolean whiteTurn;
	
	// For undoing the last move
	private GameState previousState;
	private boolean canUndo;
	
	// Information related to en passant
	private Location ep_capture;
	private Pawn ep_pawn;
	
	// Castling information
	private King K, k;
	
	// Moves when in check
	private HashSet<Move> escape_moves;
	
	/**
	 * Create a new game on the given board. Moves will automatically be
	 * recorded.
	 * @param grid The board on which the game will be played.
	 */
	public Game(Chessboard grid) {
		this(grid, true);
	}
	
	/**
	 * Creates a new game on the given board.
	 * @param grid The board on which the game will be played.
	 * @param recordable True if games should be recorded; false otherwise.
	 */
	public Game(Chessboard grid, boolean recordable) {
		super();
		this.grid = grid;
		this.black_locs = new HashSet<Location>(32);
		this.white_locs = new HashSet<Location>(32);
		this.whiteTurn = true;
		this.isRecording = recordable;
		this.moves = new MoveSequence();
		this.canUndo = false;
		defaultPosition();
		updateControlledLocations();
		for(Piece p : pieces) {
			p.update();
		}
	}
	
	/**
	 * @return True if you can undo the last move; false otherwise.
	 */
	public boolean canUndo() {
		return canUndo;
	}
	
	/**
	 * @return All active, non-captured pieces still on the board.
	 */
	public ArrayList<Piece> getActivePieces() {
		return pieces;
	}

	/**
	 * @return The en passant location, or <code>null</code> if no appropriate
	 * move has been made.
	 */
	public Location getEnPassantLocation() {
    	return ep_capture;
    }
	
	/**
	 * @return The pawn that just made a two-square move, or <code>null</code>
	 * if not applicable.
	 */
	public Pawn getEnPassantPawn() {
    	return ep_pawn;
    }

	/**
	 * @return The grid on which this game is played.
	 */
	public Chessboard getGrid() {
		return grid;
	}
	
	/**
	 * @return The set of all locations that are attacked by a black piece.
	 * Locations occupied by a black piece but not defended by another piece
	 * are not included in this set.
	 */
	public HashSet<Location> getLocsControlledByBlack() {
		return black_locs;
	}
	
	/**
	 * @return The set of all locations that are attacked by a white piece.
	 * Locations occupied by a white piece but not defended by another piece
	 * are not included in this set.
	 */
	public HashSet<Location> getLocsControlledByWhite() {
		return white_locs;
	}
	
	public boolean isCheckmate() {
		return isInCheck() && escape_moves.isEmpty();
	}
	
	/**
	 * @return True if it's white's turn; false for black.
	 */
	public boolean isWhitesTurn() {
		return whiteTurn;
	}
	
	/**
	 * Automatically makes a move for the current player. This randomly selects
	 * one of the valid moves and plays it. If no move is available, this
	 * method does nothing.
	 * TODO change that behavior?
	 */
	public void makeAutomaticMove() {
		ArrayList<Move> moves = new ArrayList<Move>();
		for(Piece p : this.pieces) {
			if(isWhitesTurn() != p.isWhite())
				continue;
			
			for(Location loc : p.getMoves()) {
				if(loc != null)
					moves.add(new Move(p.getLocation(), loc));
			}
		}
		
		if(moves.isEmpty())
			throw new NoSuchElementException("No moves.");
		Move m = moves.get((int)(Math.random() * moves.size()));
		// TODO possible castle, possible pawn promotion
		move(m.getSource(), m.getDestination());
	}
	
	/**
	 * Moves the piece at the source location to the specified destination. <br>
	 * Precondition: There exists a piece at location <code>src</code>.
	 * @param src The starting location of the piece.
	 * @param dest The desired location to move the piece.
	 * @return True if the enemy king is put in check; false otherwise.
	 * @throws InvalidLocationException If either location is not in range.
	 * @throws IllegalMoveException If the attempted move is not valid.
	 */
	public boolean move(Location src, Location dest) throws InvalidLocationException, IllegalMoveException {
		System.out.println("[Move] Start.");
		
		if(!grid.isValid(src))
			throw new InvalidLocationException(src);
		if(!grid.isValid(dest))
			throw new InvalidLocationException(dest);
		
		Piece p = grid.get(src);
		if(p instanceof Pawn && dest.getRank() == (p.isWhite() ? 7 : 0)) {
			// Pawn promotion
			return move(src, dest, 'Q');
		}
		if(p instanceof King && !p.getAttackedLocations().contains(dest)) {
			System.out.println("[Move] Castling attempt?");
			// Let's see if a castle is attempted.
			King k = (King) p;
			Rook r;
			Piece temp;
			if(dest.equals(new Location("g1"))) 
				temp = grid.get(new Location("h1"));
			else if(dest.equals(new Location("c1")))
				temp = grid.get(new Location("a1"));
			else if(dest.equals(new Location("c8")))
				temp = grid.get(new Location("a8"));
			else if(dest.equals(new Location("g8")))
				temp = grid.get(new Location("h8"));
			else
				throw new IllegalMoveException(p, dest);
			if(!(temp instanceof Rook))
				throw new IllegalMoveException(p, dest);

			r = (Rook) temp;
			if(!k.canCastle(r)) {
				System.out.println("[Move] Castling move is illegal.");
				throw new IllegalMoveException("That castling move is not allowed.", p, dest);
			}
			
			// Save the old state.
			previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()),
					ep_capture, ep_pawn); 
			
			// Let's make the castling move.
			ep_capture = null;
			ep_pawn = null;
			int dir = r.getLocation().getFile() > k.getLocation().getFile() ? 90 : -90;
			Location loc = k.getLocation().getAdjacentLocation(dir).getAdjacentLocation(dir);
			grid.put(loc, k);
			grid.put(loc.getAdjacentLocation(-dir), r);
			
			// Update
			for (Piece piece : pieces)
				piece.getMoves();
			updateControlledLocations();
			
			// Adding to the moves list
			Move.Special spec = (dest.getFile() == 2 ? Move.Special.QUEENSIDE_CASTLE
					: Move.Special.KINGSIDE_CASTLE);
			
			if(isRecording)
				moves.append(new Move(src, dest, spec));
		}
		else {
			System.out.println("[Move] Standard Move");
			if(!p.canMove(dest))
				throw new IllegalMoveException(p, dest);
			Move m = new Move(src, dest);
			if(isInCheck() && !escape_moves.contains(m)) {
				System.out.println(m);
				throw new InCheckException(escape_moves);
			}
			
			// Set up the move
			Move move = new Move(src, dest);
			
			// Store the previous states
			GameState prev = previousState;
			previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()),
					ep_capture, ep_pawn);
			
			// Time to actually move the piece.
			Piece target = grid.put(dest, p);
			if(p instanceof Pawn) {
				if (dest.equals(previousState.ep_loc)) {
					// En passant capture
					target = previousState.ep_pawn;
					grid.remove(ep_pawn.getLocation());
					move.setSpecial(Move.Special.EN_PASSANT);
				}
				else if (Math.abs(src.getRank() - dest.getRank()) == 2) {
					// En passant move
					ep_capture = new Location(dest.getRank() + (p.isWhite() ? -1 : 1), src.getFile());
					ep_pawn = (Pawn) p;
					System.out.println("[New] En-passant set to " + p + ", " + ep_capture);
				}
			}
			if(target != null) {
				pieces.remove(target);
			}
			updateControlledLocations();
			
			// Cannot make moves that put your own king in check.
			if(isInCheck()) {
				ep_capture = previousState.ep_loc;
				ep_pawn = previousState.ep_pawn;
				grid.put(src, p);
				previousState = prev;
				if(target != null) {
					grid.put(dest, target);
					pieces.add(target);
				}
				else
					grid.remove(dest);
				updateControlledLocations();
				throw new IllegalMoveException("Moving " + p + " will place the king in check.",
						p, dest);
			}
		
			// Adding to the moves list.
			if(isRecording)
				moves.append(move);
		}
		
		
		// Switch turns.
		whiteTurn = !whiteTurn;
		canUndo = true;
		
		boolean inCheck = isInCheck();
		for(Piece piece : pieces)
			piece.update();
		
		if(inCheck) {
			escape_moves = getEscapeMoves();
//			moves.append(escape_moves.isEmpty() ? '#' : '+'); 
		}
		else {
			escape_moves = null;
			updateAllowedMoves();
		}
		
		return inCheck;
	}
	
	/**
	 * Moves a piece from the source to the destination. Attempts to move
	 * a pawn and promote it.
	 * Precondition: There exists a piece at location <code>src</code>.
	 * @param src The starting location of the piece.
	 * @param dest The desired location to move the piece.
	 * @throws InvalidLocationException If either location is not in range.
	 * @throws IllegalMoveException If the attempted move is not valid.
	 * @throws IllegalStateException If a pawn promotion is attempted but is
	 * not legal.
	 * @throws IllegalArgumentException If the promotion piece is not valid.
	 * (Must be a major or minor piece.)
	 */
	public boolean move(Location src, Location dest, char promotion) {
		if(!grid.isValid(src))
			throw new InvalidLocationException(src);
		if(!grid.isValid(dest))
			throw new InvalidLocationException(dest);
		
		Piece p = grid.get(src);
		if(!(p instanceof Pawn))
			throw new IllegalArgumentException("\"" + promotion + "\" is not a proper input "
					+ " for this move.");
		if(dest.getRank() != 7)
			throw new IllegalMoveException("That's not a valid promotion.", p, dest);
		Pawn pawn = (Pawn) p;
		if(!pawn.canMove(dest))
			throw new IllegalMoveException(pawn, dest);
		
		Piece upgrade;
		switch(Character.toUpperCase(promotion)) {
			case 'Q':
				upgrade = new Queen(pawn.isWhite(), this, dest);
				break;
			case 'R':
				upgrade = new Rook(pawn.isWhite(), this, dest);
				break;
			case 'B':
				upgrade = new Bishop(pawn.isWhite(), this, dest);
				break;
			case 'N':
				upgrade = new Knight(pawn.isWhite(), this, dest);
				break;
			default:
				throw new IllegalArgumentException("" + promotion + " does not stand for a "
						+ " valid piece you may promote to. Must be Q, R, B, or N.");
		}
		
		// Save the previous state.
		previousState = new GameState(whiteTurn, new Chessboard(grid.getEntrySet()), ep_capture, ep_pawn);
		
		// Make the move.
		Piece target = grid.put(dest, upgrade);
		grid.remove(src);
		if(target != null) {
			pieces.remove(target);
		}
		
		// Switch turns.
		whiteTurn = !whiteTurn;
		canUndo = true;
		
		// Adding to the moves list.
		if(isRecording)
			moves.append(new Move(src, dest, Move.Special.parseChar(promotion)));
		
		boolean inCheck = isInCheck();
		for(Piece piece : pieces)
			piece.update();
		
		if(inCheck) {
			escape_moves = getEscapeMoves();
//			moves.append(escape_moves.isEmpty() ? '#' : '+'); 
		}
		else {
			escape_moves = null;
			updateAllowedMoves();
		}
		
		return inCheck;
	}
	
	/**
	 * @param grid Swaps out this game's chessboard for the given board.
	 */
	public void setGrid(Chessboard grid) {
		this.grid = grid;
		pieces.clear();
		for(Entry<Location, Piece> e : grid.getEntrySet()) {
			e.getValue().setLocation(e.getKey());
			pieces.add(e.getValue());
		}
	}
	
	/**
	 * Saves this game in a format that can be played again.
	 * @param title A name for this game.
	 * @return An object that can replay the game, or <code>null</code> if the
	 * game is not recording.
	 */
	public Playback toPlayback(String title) {
		if(!isRecording)
			return null;
		else
			return new Playback(moves, title);
	}
	
	/**
	 * @return All of the moves played, in extended notation. If no move has
	 * been played, this method returns <code>"No moves played."</code>
	 */
	@Override
	public String toString() {
		if(moves.isEmpty()) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		moves.reset();
		
		int i = 1;
		while (moves.currentMove() != null) {
			if(i % 2 == 1) {
				sb.append((i + 1) / 2);
				sb.append(". ");
			}
			
			sb.append(moves.currentMove().toString());
			sb.append(' ');
			moves.nextMove();
			i++;
		}
		
		return sb.toString();
	}

	/**
	 * Takes back the last move played.
	 */
	public void undo() {
		if(!canUndo)
			return;
		
		// Revert to the previous state.
		setGrid(previousState.board);
		ep_capture = previousState.ep_loc;
		ep_pawn = previousState.ep_pawn;
		whiteTurn = previousState.isWhitesTurn;
		moves.delete();
		
		// Prevent another takeback.
		previousState = null;
		canUndo = false;

	}
	
	/**
	 * Sets up a brand new game of chess. Creates a new pieces and sets up the
	 * pieces on the grid.
	 */
	private void defaultPosition() {
		pieces = new ArrayList<Piece>(32);
		// Kings
		K = new King(true, this, new Location("e1"));
		k = new King(false, this, new Location("e8"));
		pieces.add(k); pieces.add(K);
		
		// Pawns
		for(int i = 0; i < 8; i++) {
			Pawn p1 = new Pawn(true, this, new Location("" + (char)('a' + i) + "2"));
			Pawn p2 = new Pawn(false, this, new Location("" + (char)('a' + i) + "7"));
			pieces.add(p1);
			pieces.add(p2);
		}
		
		// Knights
		Knight n1, n2, n3, n4;
		n1 = new Knight(true, this, new Location("b1"));
		n2 = new Knight(true, this, new Location("g1"));
		n3 = new Knight(false, this, new Location("b8"));
		n4 = new Knight(false, this, new Location("g8"));
		pieces.add(n1); pieces.add(n2); pieces.add(n3); pieces.add(n4);
		
		// Bishops
		Bishop b1, b2, b3, b4;
		b1 = new Bishop(true, this, new Location("c1"));
		b2 = new Bishop(true, this, new Location("f1"));
		b3 = new Bishop(false, this, new Location("c8"));
		b4 = new Bishop(false, this, new Location("f8"));
		pieces.add(b1); pieces.add(b2); pieces.add(b3); pieces.add(b4);
		
		// Rooks
		Rook r1, r2, r3, r4;
		r1 = new Rook(true, this, new Location("a1"));
		r2 = new Rook(true, this, new Location("h1"));
		r3 = new Rook(false, this, new Location("a8"));
		r4 = new Rook(false, this, new Location("h8"));
		pieces.add(r1); pieces.add(r2); pieces.add(r3); pieces.add(r4);
		
		// Queens
		Queen q1, q2;
		q1 = new Queen(true, this, new Location("d1"));
		q2 = new Queen(false, this, new Location("d8"));
		pieces.add(q1); pieces.add(q2);
		
		for(Piece p : pieces)
			grid.put(p.getLocation(), p);
	}
	
	/**
	 * @return A set of valid moves that takes the king out of check.
	 */
	private HashSet<Move> getEscapeMoves() {
		HashSet<Move> result = new HashSet<Move>(3);
		ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
		for(Piece p : pieces)
			all_pieces.add(p);
		
		for(Piece p : all_pieces) {
			if(p.isWhite() != isWhitesTurn())
				continue;
			Location prev = p.getLocation();
			for(Location loc : p.getMoves()) {
				Piece captured = grid.put(loc, p);
				if(captured != null)
					pieces.remove(captured);
				updateControlledLocations();
				if(!isInCheck())
					result.add(new Move(p.getLocation(), loc));
				grid.put(prev, p);
				if(captured != null) {
					grid.put(loc, captured);
					pieces.add(captured);
				}
				else
					grid.remove(loc);
				updateControlledLocations();
			}
		}
		return result;
	}
	
	/**
	 * @return True if the current player moving is in check; false otherwise.
	 */
	private boolean isInCheck() {
		return isWhitesTurn() ? getLocsControlledByBlack().contains(K.getLocation()) :
			getLocsControlledByWhite().contains(k.getLocation());
	}
	
	private void updateAllowedMoves() {
		HashSet<Location> enemy = isWhitesTurn() ? black_locs : white_locs;
		King our_king = isWhitesTurn() ? K : k;
		ArrayList<Piece> all_pieces = new ArrayList<Piece>(pieces.size());
		for(Piece p : pieces) {
			all_pieces.add(p);
		}
		
		for(Piece p : all_pieces) {
			if((isWhitesTurn() != p.isWhite()) || p instanceof King)
				continue;
			
			Location prev = p.getLocation();
			Iterator<Location> i = p.getMoves().iterator();
			while(i.hasNext()) {
				Location loc = i.next();
				Piece captured = grid.put(loc, p);
				if(captured != null)
					pieces.remove(captured);
				updateControlledLocations();
				if(enemy.contains(our_king.getLocation())) {
					System.out.println("Removing location " + loc + " for " + p);
					i.remove();
				}
				grid.put(prev, p);
				if(captured != null) {
					grid.put(loc, captured);
					pieces.add(captured);
				}
				else
					grid.remove(loc);
				updateControlledLocations();
			}
		}
	}
	
	private void updateControlledLocations() {
		white_locs.clear();
		black_locs.clear();
		for(Piece p : pieces) {
			if(p.isWhite())
				white_locs.addAll(p.getAttackedLocations());
			else
				black_locs.addAll(p.getAttackedLocations());
		}
	}
}
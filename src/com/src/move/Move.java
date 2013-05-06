package com.src.move;

import com.src.grid.Location;

/**
 * Represents a move on a chessboard.
 * @author Kyle
 */
public class Move {
	/**
	 * Indicates a special type of move.
	 * @author kyle
	 */
	public static enum Special {
		/**
		 * Indicates that the move is a pawn underpromotion to a bishop.
		 */
		BISHOP("B"),
		
		/**
		 * Indicates that the move put the opponent in check.
		 */
		CHECK("+"),
		
		/**
		 * Indicates that the move put the opponent in checkmate.
		 */
		CHECKMATE("#"),
		
		/**
		 * Indicates that the move is an en passant capture.
		 */
		EN_PASSANT("e.p."),
		
		/**
		 * Indicates that the move is a kingside castle.
		 */
		KINGSIDE_CASTLE("O-O"),
		
		/**
		 * Indicates that the move is a pawn underpromotion to a knight.
		 */
		KNIGHT("N"),
		
		/**
		 * Indicates that this move is a normal move.
		 */
		NONE(""),
		
		/**
		 * Indicates that the move is a pawn promotion to a queen.
		 */
		QUEEN("Q"),
		
		/**
		 * Indicates that the move is a queensde castle.
		 */
		QUEENSIDE_CASTLE("O-O-O"),
		
		/**
		 * Indicates that the move is a pawn underpromotion to a rook.
		 */
		ROOK("R");
		
		private String desc;
		private Special(String desc) {
			this.desc = desc;
		}
		
		public static Special parseChar(char c) {
			switch(Character.toLowerCase(c)) {
				case 'q':
					return QUEEN;
				case 'r':
					return ROOK;
				case 'n':
					return KNIGHT;
				case 'b':
					return BISHOP;
				default:
					throw new IllegalArgumentException("Unexpected argument.");
			}
		}
	}
	
	private Special special;
	private Location src, dest;
	
	/**
	 * Creates an object to represent a single move.
	 * @param src The source location of the piece moving.
	 * @param loc Its destination location.
	 */
	public Move(Location src, Location dest) {
		this(src, dest, Special.NONE);
	}
	
	/**
	 * Creates an object to represent a single move. The special field
	 * indicates a special type of move.
	 * @param src The source location of the piece moving.
	 * @param loc Its destination location.
	 * @param spec The type of special move.
	 */
	public Move(Location src, Location dest, Special spec) {
		super();
		this.src = src;
		this.dest = dest;
		this.special = spec;
	}
	
	/**
	 * Creates an object to represent a single move.
	 * @param src The source location of the piece moving.
	 * @param loc Its destination location.
	 */
	public Move(String src, String dest) {
		this(src, dest, Special.NONE);
	}
	
	/**
	 * Creates an object to represent a single move. The special field
	 * indicates a special type of move.
	 * @param src The source location of the piece moving.
	 * @param loc Its destination location.
	 * @param spec The type of special move.
	 */
	public Move(String src, String dest, Special spec) {
		super();
		this.src = new Location(src);
		this.dest = new Location(dest);
		this.special = spec;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Move))
			return false;
		
		Move m = (Move) o;
		return m.src.equals(src) && m.dest.equals(dest);
	}
	
	/**
	 * @return The destination location.
	 */
	public Location getDestination() {
		return dest;
	}
	
	/**
	 * @return The source location.
	 */
	public Location getSource() {
		return src;
	}
	
	@Override
	public String toString() {
		String str =  getSource() + "-" + getDestination();
		switch(special) {
			case BISHOP:
			case QUEEN:
			case KNIGHT:
			case ROOK:
				str += "=" + special.desc;
				break;
			case KINGSIDE_CASTLE:
			case QUEENSIDE_CASTLE:
				return special.desc;
			case EN_PASSANT:
				str += " " + special.desc;
				break;
			case CHECK:
			case CHECKMATE:
				str += special.desc;
			default:
				break;
		}
		return str;
	}
}
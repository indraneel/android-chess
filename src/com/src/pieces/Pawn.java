package com.src.pieces;


import java.util.ArrayList;
import java.util.Iterator;

import com.src.game.Game;
import com.src.grid.Location;

/**
 * Pawns form the skeleton of a player's game. They can move one space forward,
 * or two on their first move. Pawns cannot move backwards and only capture
 * diagonally. They also have two special moves: en passant capture and
 * promotion upon reaching the eighth rank.
 * @author Kyle
 */
public class Pawn extends Piece {
	private Location start, ep;

	public Pawn(boolean isWhite, Game game, Location loc) {
		super(isWhite, game, loc);
		start = loc;
		ep = getLocation().getAdjacentLocation(isWhite ? 0 : 180);
	}
	
	private Pawn(boolean isWhite, Game game) {
		super(isWhite, game, null);
	}
	
	@Override
	public Piece copy() {
		Pawn p = new Pawn(isWhite(), getGame());
		p.overrideLocation(getLocation().copy());
		p.start = start.copy();
		p.ep = ep.copy();
		return p;
	}
	
	@Override
	public String getAlgebraicName() {
		return "";
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>(2);
		Location loc = getLocation();
		Location l1 = new Location(loc.getRank() + 1, loc.getFile() - 1),
				 l2 = new Location(l1.getRank(), loc.getFile() + 1);
		
		if(getGrid().isValid(l1)) {
			locs.add(l1);
		}
		if(getGrid().isValid(l2)) {
			locs.add(l2);
		}
		return locs;
	}
	
	@Override
	public int getValue() {
		return 1;
	}

	/**
	 * @return True if this pawn has already moved; false otherwise. If the
	 * piece has been captured then the result of this method is undefined.
	 */
	public boolean hasMoved() {
		if(start == null)
			return false;
		return !start.equals(getLocation());
	}

	/**
	 * Promotes this pawn for reaching the eighth rank.
	 * @param p The piece to promote to. If this is set to null, the piece is
	 * assumed to be a queen.
	 * @throws IllegalArgumentException If the piece to promote to is illegal 
	 * (must be a minor or major piece).
	 * @throws IllegalStateException If the pawn is not on its eighth rank.
	 */
	public void promote(Piece p) {
		if(getLocation().getRank() != (isWhite() ? 7 : 0))
			throw new IllegalStateException("Cannot promote when not on the eighth rank.");
		
		if(p == null)
			p = new Queen(isWhite(), getGame(), getLocation());
		if(p instanceof King || p instanceof Pawn)
			throw new IllegalArgumentException
				("You must promote to a queen, rook, knight, or bishop.");
		
		p.setLocation(getLocation());
		setLocation(null);
	}
	
	@Override
	public void setLocation(Location loc) {
		if(loc != null && getLocation() != null) {
			int delta_y = Math.abs(loc.getRank() - getLocation().getRank());
			if(delta_y == 2)
				getGame().setEnPassant(this, ep);
		}
		super.setLocation(loc);
	}

	@Override
	public char toFEN() {
		return isWhite() ? 'P' : 'p';
	}
	
	@Override
	protected ArrayList<Location> getMoveLocations() {
		// First let's deal with the diagonal squares.
		ArrayList<Location> result = getAttackedLocations();
		Iterator<Location> i = result.iterator();
		while(i.hasNext()) {
			Location loc = i.next();
			if(getGrid().isOccupied(loc)) {
				Piece p = getGrid().get(loc);
				if(isSameColor(p))
					i.remove();
			}
			else {
				// Being extra safe
				Location ep = getGame().getEnPassantLocation();
				if(ep == null || !ep.equals(loc) || isSameColor(getGame().getEnPassantPawn()))
					i.remove();
			}
		}
		
		// Next come the squares in front.
		int dir = isWhite() ? 0 : 180;
		Location l1 = getLocation().getAdjacentLocation(dir);
		if(getGrid().isValid(l1) && !getGrid().isOccupied(l1)) {
			result.add(l1);
			Location l2 = l1.getAdjacentLocation(dir);
			if(!hasMoved() && !getGrid().isOccupied(l2)) {
				result.add(l2);
			}
		}
		return result;
	}
	
	private void overrideLocation(Location loc) {
		super.setLocation(loc);
	}
}
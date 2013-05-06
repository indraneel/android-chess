package com.src.pieces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.src.game.Game;
import com.src.grid.Location;


/**
 * Put the enemy's king in checkmate to win the game. Though the king must
 * never be captured and thus has infinite value, in the endgame it has
 * attacking and defending power of about four points (stronger than a minor
 * piece but weaker than a rook).
 * @author Kyle Suarez
 */
public class King extends Piece {
	private Location start;

	public King(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		start = location;
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'K' : 'k';
	}
	
	@Override
	public Piece copy() {
		King k = new King(isWhite(), getGame(), getLocation().copy());
		k.start = start.copy();
		return k;
	}
	
	/**
	 * @param r Either the kingside or queenside rook.
	 * @return True if this king can castle with the rook; false otherwise.
	 */
	public boolean canCastle(Rook r) {
		if(hasMoved() || r.hasMoved() || isWhite() != r.isWhite())
			return false;
		
		int dir = r.getLocation().getFile() > getLocation().getFile() ? 90 : -90;
		ArrayList<Location> locs = new ArrayList<Location>();
		Location loc = getLocation();
		for(int i = 0; i < 3; i++) {
			locs.add(loc);
			loc = loc.getAdjacentLocation(dir);
		}
		
		HashSet<Location> attacked = isWhite() ? getGame().getLocsControlledByBlack() : 
			getGame().getLocsControlledByWhite();
		
		for(Location l : locs) {
			if(attacked.contains(l) || getGrid().isOccupied(l))
				return false;
		}
		
		return true;
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>(8);
		for(int dir = 0; dir < 360; dir += 45) {
			Location l = getLocation().getAdjacentLocation(dir);
			if(getGrid().isValid(l)) {
				locs.add(l);
			}
		}
		return locs;
	}

	@Override
	protected ArrayList<Location> getMoveLocations() {
		ArrayList<Location> locs = getAttackedLocations();
		HashSet<Location> invalid = isWhite() ? 
				getGame().getLocsControlledByBlack() : getGame().getLocsControlledByWhite();
				
		Iterator<Location> i = locs.iterator();
		while(i.hasNext()) {
			Location loc = i.next();
			if(invalid.contains(loc) || (getGrid().isOccupied(loc) && isSameColor(getGrid().get(loc))))
				i.remove();
		}
		return locs;
	}
	
	@Override
	public int getValue() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * @return True if this king has moved already; false otherwise.
	 */
	public boolean hasMoved() {
		if(start == null)
			return false;
		return !start.equals(getLocation());
	}
}
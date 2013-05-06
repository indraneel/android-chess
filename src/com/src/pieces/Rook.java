package com.src.pieces;


import java.util.ArrayList;

import com.src.game.Game;
import com.src.grid.Location;

/**
 * Rooks can move in any direction along ranks or files, moving to an empty
 * location or capturing an enemy piece. They are worth about five pawns.
 * @author Kyle
 */
public class Rook extends Piece {
	private Location start;

	public Rook(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
		start = location;
	}
	
	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 90) {
			Location l = getLocation().getAdjacentLocation(dir);
			while(getGrid().isValid(l)) {
				locs.add(l);
				if(getGrid().isOccupied(l))
					break;
				l = l.getAdjacentLocation(dir);
			}
		}
		return locs;
	}
	
	@Override
	public ArrayList<Location> getMoveLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 90) {
			Location l = getLocation().getAdjacentLocation(dir);
			while(getGrid().isValid(l)) {
				if(getGrid().isOccupied(l)) {
					if(!isSameColor(getGrid().get(l)))
						locs.add(l);
					break;	
				}
				locs.add(l);
				l = l.getAdjacentLocation(dir);
			}
		}
		return locs;
	}
	
	@Override
	public int getValue() {
		return 5;
	}

	/**
	 * @return True if the rook has already moved; false otherwise.
	 */
	public boolean hasMoved() {
		if(start == null)
			return false;
		return !start.equals(getLocation());
	}

	@Override
	public char toFEN() {
		return isWhite() ? 'R' : 'r';
	}
}
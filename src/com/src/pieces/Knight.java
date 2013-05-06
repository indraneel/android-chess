package com.src.pieces;


import java.util.ArrayList;

import com.src.game.Game;
import com.src.grid.Location;

/**
 * Knights move in an L-shape and have the ability to jump over other pieces.
 * They are about equal in power to a bishop, depending on the position, and
 * are worth approximately three pawns.
 * @author Kyle
 */
public class Knight extends Piece {

	public Knight(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'N' : 'n';
	}

	@Override
	public ArrayList<Location> getMoveLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 90) {
			Location l = getLocation().getAdjacentLocation(dir).getAdjacentLocation(dir);
			Location loc1 = l.getAdjacentLocation(dir + 90),
					 loc2 = l.getAdjacentLocation(dir - 90);
			
			try {
			if(getGrid().isValid(loc1) && (!getGrid().isOccupied(loc1) || !isSameColor(getGrid().get(loc1))))
				locs.add(loc1);
			if(getGrid().isValid(loc2) && (!getGrid().isOccupied(loc2) || !isSameColor(getGrid().get(loc2))))
				locs.add(loc2);
			}
			catch(NullPointerException e) {
				System.out.println("Null pointer exception found for " + this);
				System.out.println("At location " + loc1 + ": " + getGrid().get(loc1));
				System.out.println("At location " + loc2 + ": " + getGrid().get(loc2));
			}
		}
		return locs;
	}

	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int dir = 0; dir < 360; dir += 90) {
			Location l = getLocation().getAdjacentLocation(dir).getAdjacentLocation(dir);
			locs.add(l.getAdjacentLocation(dir + 90));
			locs.add(l.getAdjacentLocation(dir - 90));
		}
		
		ArrayList<Location> bad = new ArrayList<Location>(locs.size());
		for(Location l : locs) {
			if(!getGrid().isValid(l))
				bad.add(l);
		}
		locs.removeAll(bad);
		return locs;
	}

	@Override
	public int getValue() {
		return 3;
	}
}
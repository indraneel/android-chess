package com.src.pieces;


import java.util.ArrayList;

import com.src.game.Game;
import com.src.grid.Location;

/**
 * Bishops move along diagonals and are about equal in power to a Knight,
 * depending on the position. Their value is placed at about three pawns.  
 * @author Kyle
 */
public class Bishop extends Piece {

	public Bishop(boolean isWhite, Game game, Location location) {
		super(isWhite, game, location);
	}
	
	@Override
	public char toFEN() {
		return isWhite() ? 'B' : 'b';
	}

	@Override
	public ArrayList<Location> getAttackedLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		for(int i = 0; i < 4; i++) {
			int dir = 45 + 90*i;
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
		for(int i = 0; i < 4; i++) {
			int dir = 45 + 90*i;
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
		return 3;
	}
}